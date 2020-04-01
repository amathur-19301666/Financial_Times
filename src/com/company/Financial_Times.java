package com.company;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Financial_Times {

    private static List<Document> financial_times_document_list = new ArrayList<>();
    private static boolean headline_flag = false, text_flag = false, by_line_flag = false;

    public static List<Document> load_financial_times_docs(List<String> financial_times_files) throws IOException {
        Financial_Times_Object financial_times_object = new Financial_Times_Object();

        for(String file : financial_times_files){

            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String current_line;


                while((current_line = br.readLine()) != null){
                    current_line = current_line.trim();
                    financial_times_object = set_object_data(current_line, financial_times_object);
                }
                financial_times_object = create_new_object(financial_times_object);

                try {
                    br.close();
                } catch (IOException e) {
                    System.out.println(String.format("ERROR: IOException occurred when closing file: %s", file));
                    System.out.println(String.format("ERROR MESSAGE: %s", e.getMessage()));
                }

            } catch (FileNotFoundException e) {
                System.out.println(String.format("ERROR: FileNotFoundException occurred when trying to read file: %s",
                        file));
                System.out.println(String.format("ERROR MESSAGE: %s", e.getMessage()));
            }
        }

        return financial_times_document_list;
    }

    private static Financial_Times_Object set_object_data(String current_line, Financial_Times_Object financial_times_object) throws NullPointerException{
        if(current_line.contains(Financial_Times_Tags.DOC_END.get_tag())){
            financial_times_object = create_new_object(financial_times_object);
        } else if (current_line.contains(Financial_Times_Tags.DOC_NO_START.get_tag())){
            financial_times_object.set_doc_no(parse_document(current_line, "doc_no"));
        } else if (current_line.equals(Financial_Times_Tags.HEADLINE_START.get_tag())){
            headline_flag = true;
        } else if(current_line.contains(Financial_Times_Tags.HEADLINE_END.get_tag())){
            headline_flag = false;
        } else if (current_line.contains(Financial_Times_Tags.BYLINE_START.get_tag())){
            by_line_flag = true;
        } else if (current_line.contains(Financial_Times_Tags.BYLINE_END.get_tag())){
            by_line_flag = false;
        } else if (current_line.contains(Financial_Times_Tags.TEXT_START.get_tag())){
            text_flag = true;
        } else if (current_line.contains(Financial_Times_Tags.TEXT_END.get_tag())){
            text_flag = false;
        } else if (current_line.contains(Financial_Times_Tags.DOC_ID_START.get_tag())){
            financial_times_object.setDocId(parse_document(current_line, "doc_id"));
        }

        if(headline_flag){
            financial_times_object.set_headline(financial_times_object.get_headline() + " " + parse_document(current_line,
                    "headline"));
        } else if(text_flag){
            financial_times_object.set_text(financial_times_object.get_text() + " " + parse_document(current_line,
                    "text"));
        } else if(by_line_flag){
            financial_times_object.set_by_line(financial_times_object.get_by_line() + " " + parse_document(current_line,
                    "byline"));
        }
        return financial_times_object;
    }

    private static Financial_Times_Object create_new_object(Financial_Times_Object financial_times_object){
        Document financial_times_document = create_new_document(financial_times_object);
        financial_times_document_list.add(financial_times_document);
        return new Financial_Times_Object();
    }

    private static String parse_document(String currLine, String textField){
        switch (textField){
            case "doc_id":
                return currLine.replaceAll(Financial_Times_Tags.DOC_ID_START.get_tag(), "").replaceAll(
                        Financial_Times_Tags.TEXT_END.get_tag(), "");
            case "text":
                return currLine.replaceAll(Financial_Times_Tags.TEXT_START.get_tag(), "").replaceAll(
                        Financial_Times_Tags.TEXT_END.get_tag(), "");
            case "byline":
                return currLine.replaceAll(Financial_Times_Tags.BYLINE_START.get_tag(), "").replaceAll(
                        Financial_Times_Tags.BYLINE_END.get_tag(), "");
            case "headline":
                return currLine.replaceAll(Financial_Times_Tags.HEADLINE_START.get_tag(), "").replaceAll(
                        Financial_Times_Tags.HEADLINE_END.get_tag(), "");
            case "doc_no":
                return currLine.replaceAll(Financial_Times_Tags.DOC_NO_START.get_tag(), "").replaceAll(
                        Financial_Times_Tags.DOC_NO_END.get_tag(), "");
            default:
                return null;
        }
    }

    private static Document create_new_document(Financial_Times_Object financial_times_object) {
        Document document = new Document();

        document.add(new StringField("doc_no", financial_times_object.get_doc_no(), Field.Store.YES));
        document.add(new TextField("headline", financial_times_object.get_headline(), Field.Store.YES));
        document.add(new TextField("text", financial_times_object.get_text(), Field.Store.YES));

        return document;
    }
}