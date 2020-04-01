package com.company;

public enum Financial_Times_Tags {
    TEXT_START("<TEXT>"), TEXT_END("</TEXT>"), HEADLINE_START("<HEADLINE>"), HEADLINE_END("</HEADLINE>"), BYLINE_START(
            "<BYLINE>"), BYLINE_END("</BYLINE>"), CORRECTION_START("<CORRECTION>"), CORRECTION_END(
            "</CORRECTION>"), CORRECTION_DATE_START("<CORRECTION-DATE>"), CORRECTION_DATE_END(
            "</CORRECTION_DATE>"), DOC_NO_START("<DOCNO>"), DOC_ID_START("<DOCID>"), DOC_START(
            "<DOC>"), DOC_END("</DOC>"), DOC_ID_END("</DOCID>"), DOC_NO_END("</DOCNO>");;

    String tag;

    Financial_Times_Tags(final String tag) {
        this.tag = tag;
    }

    public String get_tag() {
        return this.tag;
    }
}