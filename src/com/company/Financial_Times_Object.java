package com.company;

public class Financial_Times_Object {
    private String doc_no;
    private String doc_id;
    private String headline;
    private String by_line;
    private String text;
    private String date;

    Financial_Times_Object(){
        this.doc_no = "";
        this.doc_id = "";
        this.headline = "";
        this.by_line = "";
        this.text = "";
        this.date = "";
    }

    String get_doc_no() { return doc_no; }
    void set_doc_no(String doc_no) { this.doc_no = doc_no; }

    String get_doc_id() { return doc_id; }
    void setDocId(String doc_id) { this.doc_id = doc_id; }

    String get_headline() { return headline; }
    void set_headline(String headline) { this.headline = headline; }

    String get_by_line() { return by_line; }
    void set_by_line(String by_line) { this.by_line = by_line; }

    String get_text() { return text; }
    void set_text(String text) { this.text = text; }

    String get_date() { return date; }
    void set_date(String date) { this.date = date; }
}

