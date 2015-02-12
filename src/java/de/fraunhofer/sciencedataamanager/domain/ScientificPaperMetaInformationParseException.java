/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fraunhofer.sciencedataamanager.domain;

/**
 *
 * @author Moritz Mars
 */
public class ScientificPaperMetaInformationParseException {
    private ScientificPaperMetaInformation scientificPaperMetaInformation;
    private Exception parseException;
    private String parseState;

    public Exception getParseException() {
        return parseException;
    }

    public void setParseException(Exception parseException) {
        this.parseException = parseException;
    }

    public String getParseState() {
        return parseState;
    }

    public void setParseState(String parseState) {
        this.parseState = parseState;
    }
    
    
    public ScientificPaperMetaInformation getScientificPaperMetaInformation() {
        return scientificPaperMetaInformation;
    }

    public void setScientificPaperMetaInformation(ScientificPaperMetaInformation scientificPaperMetaInformation) {
        this.scientificPaperMetaInformation = scientificPaperMetaInformation;
    }
    
}
