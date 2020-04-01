package com.company;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.analysis.standard.StandardAnalyzer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static com.company.Financial_Times.load_financial_times_docs;


public class Main {
    private final static Path current_directory = Paths.get("").toAbsolutePath();
    private final static String dataset_path = String.format("%s/ft", current_directory);
    private static List<Document> financial_times_docs = new ArrayList<>();
    private final static String index_path = String.format("%s/indexed_files", current_directory);

    public static void main(String[] args)  throws ParseException, IOException  {
	// write your code here
        Similarity similarityModel = null;
        Analyzer analyzer = null;
        similarityModel = new BM25Similarity();
        analyzer = new StandardAnalyzer(EnglishAnalyzer.getDefaultStopSet());

        Directory directory;
        System.out.println("Starting");
        if(!new File(index_path).exists()){
            directory = FSDirectory.open(Paths.get(index_path));
            load_docs();
            index_documents(similarityModel, analyzer, directory);
        } else {
            System.out.println("Loading previously indexed data");
            directory = FSDirectory.open(Paths.get(index_path));
        }

        analyzer.close();

        directory.close();

    }

    static IndexWriterConfig setIndexWriterConfig(Similarity similarity, Analyzer analyzer){
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
        return  indexWriterConfig.setSimilarity(similarity).setOpenMode(IndexWriterConfig.OpenMode.CREATE);
    }

    static void closeIndexWriter(IndexWriter indexWriter) {
        try {
            indexWriter.close();
        } catch (IOException e) {
            System.out.println(String.format("ERROR MESSAGE: %s", e.getMessage()));
        }
    }

    private static void index_documents(Similarity similarity, Analyzer analyzer, Directory directory) {
        IndexWriter indexWriter;
        IndexWriterConfig indexWriterConfig = setIndexWriterConfig(similarity, analyzer);

        try {
            indexWriter = new IndexWriter(directory, indexWriterConfig);
            indexWriter.deleteAll();

            System.out.println("Indexing");
            indexWriter.addDocuments(financial_times_docs);

            closeIndexWriter(indexWriter);

        } catch (IOException e) {
            System.out.println(String.format("ERROR MESSAGE: %s", e.getMessage()));
        }
    }

    private static void load_docs() throws IOException {

        System.out.println("Loading Documents");
        List<String> financial_times_files = get_filenames_in_dir(dataset_path);
        financial_times_docs = load_financial_times_docs(financial_times_files);
        System.out.println("loaded financial times documents");
    }

    static List<String> get_filenames_in_dir(String dir){
        List<String> files = new ArrayList<>();
        try {
            Files.walk(Paths.get(dir)).forEach(path ->{
                File file = new File(path.toString());
                if(file.isFile() && ! file.getName().contains("read")){
                    files.add(path.toString());
                }
            });
        } catch (IOException e) {
            System.out.println(String.format("ERROR MESSAGE: %s", e.getMessage()));
        }
        return files;
    }

}
