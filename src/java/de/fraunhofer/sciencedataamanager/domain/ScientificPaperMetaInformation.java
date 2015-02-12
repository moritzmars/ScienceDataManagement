/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fraunhofer.sciencedataamanager.domain;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.LinkedList;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Moritz Mars
 */
@ManagedBean(name = "scientificPaperMetaInformation")
@SessionScoped
public class ScientificPaperMetaInformation {

    private int ID;
    private int Search_Definiton_Execution_ID;
    private int Search_Definiton_Execution_Search_Definiton_ID;
    private int Search_Definition_Execution_Run_ID;
    private int Search_Definition_Execution_Run_Search_Definiton_ID;

    private String Title;
    private String Identifier_1;
    private String Identifier_2;
    private String Identifier_3;
    private String Identifier_4;
    private String Identifier_5;
    private String Identifier_6;
    private String Identifier_7;
    private String Identifier_8;
    private String Identifier_9;
    private String Identifier_10;
    private String ScrPublisherName;
    private Date SrcPublicationDate;
    private String SrcDOI;
    private String SrcIISN;
    private int SrcVolume;
    private String ScrIdentifier_1;
    private String SrcIdentifier_2;
    private String SrcIdentifier_3;
    private String SrcIdentifier_4;
    private String SrcISBN;
    private String Url_1;
    private String Url_2;
    private String Url_3;
    private String Url_4;
    private String SrcAdditionalField_1;
    private String SrcAdditionalField_2;
    private String SrcAdditionalField_3;
    private String SrcAdditionalField_4;
    private String srcTitle;
    private String srcStartPage;
    private String srcEndPage;
    private Timestamp srcDate_1;
    private Timestamp srcDate_2;
    private Timestamp srcDate_3;
    private String srcDate_4;
    private String Text_1;
    private String Text_2;
    private String Text_3;
    private String Text_4;
    private String Text_5;
    private String Text_6;
    private String Text_7;
    private String Text_8;
    private String Text_9;
    private String Text_10;
    private LinkedList<ScientificPaperMetaInformationAuthors> Authors = new LinkedList<ScientificPaperMetaInformationAuthors>();
    private Timestamp LocalizedTime;

    public int getID() {
        return ID;
    }

    public void setID(int iD) {
        ID = iD;
    }

    public int getSearch_Definiton_Execution_ID() {
        return Search_Definiton_Execution_ID;
    }

    public void setSearch_Definiton_Execution_ID(
            int search_Definiton_Execution_ID) {
        Search_Definiton_Execution_ID = search_Definiton_Execution_ID;
    }

    public int getSearch_Definiton_Execution_Search_Definiton_ID() {
        return Search_Definiton_Execution_Search_Definiton_ID;
    }

    public void setSearch_Definiton_Execution_Search_Definiton_ID(
            int search_Definiton_Execution_Search_Definiton_ID) {
        Search_Definiton_Execution_Search_Definiton_ID = search_Definiton_Execution_Search_Definiton_ID;
    }

    public int getSearch_Definition_Execution_Run_ID() {
        return Search_Definition_Execution_Run_ID;
    }

    public void setSearch_Definition_Execution_Run_ID(int Search_Definition_Execution_Run_ID) {
        this.Search_Definition_Execution_Run_ID = Search_Definition_Execution_Run_ID;
    }

    public int getSearch_Definition_Execution_Run_Search_Definiton_ID() {
        return Search_Definition_Execution_Run_Search_Definiton_ID;
    }

    public void setSearch_Definition_Execution_Run_Search_Definiton_ID(int Search_Definition_Execution_Run_Search_Definiton_ID) {
        this.Search_Definition_Execution_Run_Search_Definiton_ID = Search_Definition_Execution_Run_Search_Definiton_ID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getIdentifier_1() {
        return Identifier_1;
    }

    public void setIdentifier_1(String identifier_1) {
        Identifier_1 = identifier_1;
    }

    public String getIdentifier_2() {
        return Identifier_2;
    }

    public void setIdentifier_2(String identifier_2) {
        Identifier_2 = identifier_2;
    }

    public String getIdentifier_3() {
        return Identifier_3;
    }

    public void setIdentifier_3(String identifier_3) {
        Identifier_3 = identifier_3;
    }

    public String getIdentifier_4() {
        return Identifier_4;
    }

    public void setIdentifier_4(String identifier_4) {
        Identifier_4 = identifier_4;
    }

    public String getIdentifier_5() {
        return Identifier_5;
    }

    public void setIdentifier_5(String identifier_5) {
        Identifier_5 = identifier_5;
    }

    public String getIdentifier_6() {
        return Identifier_6;
    }

    public void setIdentifier_6(String identifier_6) {
        Identifier_6 = identifier_6;
    }

    public String getIdentifier_7() {
        return Identifier_7;
    }

    public void setIdentifier_7(String identifier_7) {
        Identifier_7 = identifier_7;
    }

    public String getIdentifier_8() {
        return Identifier_8;
    }

    public void setIdentifier_8(String identifier_8) {
        Identifier_8 = identifier_8;
    }

    public String getIdentifier_9() {
        return Identifier_9;
    }

    public void setIdentifier_9(String identifier_9) {
        Identifier_9 = identifier_9;
    }

    public String getIdentifier_10() {
        return Identifier_10;
    }

    public void setIdentifier_10(String identifier_10) {
        Identifier_10 = identifier_10;
    }

    public String getScrPublisherName() {
        return ScrPublisherName;
    }

    public void setScrPublisherName(String scrPublisherName) {
        ScrPublisherName = scrPublisherName;
    }

    public Date getSrcPublicationDate() {
        return SrcPublicationDate;
    }

    public void setSrcPublicationDate(Date srcPublicationDate) {
        SrcPublicationDate = srcPublicationDate;
    }

    public String getSrcDOI() {
        return SrcDOI;
    }

    public void setSrcDOI(String srcDOI) {
        SrcDOI = srcDOI;
    }

    public String getSrcIISN() {
        return SrcIISN;
    }

    public void setSrcIISN(String srcIISN) {
        SrcIISN = srcIISN;
    }

    public int getSrcVolume() {
        return SrcVolume;
    }

    public void setSrcVolume(int srcVolume) {
        SrcVolume = srcVolume;
    }

    public String getScrIdentifier_1() {
        return ScrIdentifier_1;
    }

    public void setScrIdentifier_1(String scrIdentifier_1) {
        ScrIdentifier_1 = scrIdentifier_1;
    }

    public String getSrcIdentifier_2() {
        return SrcIdentifier_2;
    }

    public void setSrcIdentifier_2(String srcIdentifier_2) {
        SrcIdentifier_2 = srcIdentifier_2;
    }

    public String getSrcIdentifier_3() {
        return SrcIdentifier_3;
    }

    public void setSrcIdentifier_3(String srcIdentifier_3) {
        SrcIdentifier_3 = srcIdentifier_3;
    }

    public String getSrcIdentifier_4() {
        return SrcIdentifier_4;
    }

    public void setSrcIdentifier_4(String srcIdentifier_4) {
        SrcIdentifier_4 = srcIdentifier_4;
    }

    public String getSrcISBN() {
        return SrcISBN;
    }

    public void setSrcISBN(String srcISBN) {
        SrcISBN = srcISBN;
    }

    public String getUrl_1() {
        return Url_1;
    }

    public void setUrl_1(String url_1) {
        Url_1 = url_1;
    }

    public String getUrl_2() {
        return Url_2;
    }

    public void setUrl_2(String url_2) {
        Url_2 = url_2;
    }

    public String getUrl_3() {
        return Url_3;
    }

    public void setUrl_3(String url_3) {
        Url_3 = url_3;
    }

    public String getUrl_4() {
        return Url_4;
    }

    public void setUrl_4(String url_4) {
        Url_4 = url_4;
    }

    public String getSrcAdditionalField_1() {
        return SrcAdditionalField_1;
    }

    public void setSrcAdditionalField_1(String srcAdditionalField_1) {
        SrcAdditionalField_1 = srcAdditionalField_1;
    }

    public String getSrcAdditionalField_2() {
        return SrcAdditionalField_2;
    }

    public void setSrcAdditionalField_2(String srcAdditionalField_2) {
        SrcAdditionalField_2 = srcAdditionalField_2;
    }

    public String getSrcAdditionalField_3() {
        return SrcAdditionalField_3;
    }

    public void setSrcAdditionalField_3(String srcAdditionalField_3) {
        SrcAdditionalField_3 = srcAdditionalField_3;
    }

    public String getSrcAdditionalField_4() {
        return SrcAdditionalField_4;
    }

    public void setSrcAdditionalField_4(String srcAdditionalField_4) {
        SrcAdditionalField_4 = srcAdditionalField_4;
    }

    public String getSrcTitle() {
        return srcTitle;
    }

    public void setSrcTitle(String srcTitle) {
        this.srcTitle = srcTitle;
    }

    public String getSrcStartPage() {
        return srcStartPage;
    }

    public void setSrcStartPage(String srcStartPage) {
        this.srcStartPage = srcStartPage;
    }

    public String getSrcEndPage() {
        return srcEndPage;
    }

    public void setSrcEndPage(String srcEndPage) {
        this.srcEndPage = srcEndPage;
    }

    public Timestamp getSrcDate_1() {
        return srcDate_1;
    }

    public void setSrcDate_1(Timestamp srcDate_1) {
        this.srcDate_1 = srcDate_1;
    }

    public Timestamp getSrcDate_2() {
        return srcDate_2;
    }

    public void setSrcDate_2(Timestamp srcDate_2) {
        this.srcDate_2 = srcDate_2;
    }

    public Timestamp getSrcDate_3() {
        return srcDate_3;
    }

    public void setSrcDate_3(Timestamp srcDate_3) {
        this.srcDate_3 = srcDate_3;
    }

    public String getSrcDate_4() {
        return srcDate_4;
    }

    public void setSrcDate_4(String srcDate_4) {
        this.srcDate_4 = srcDate_4;
    }

    public String getText_1() {
        return Text_1;
    }

    public void setText_1(String text_1) {
        Text_1 = text_1;
    }

    public String getText_2() {
        return Text_2;
    }

    public void setText_2(String text_2) {
        Text_2 = text_2;
    }

    public String getText_3() {
        return Text_3;
    }

    public void setText_3(String text_3) {
        Text_3 = text_3;
    }

    public String getText_4() {
        return Text_4;
    }

    public void setText_4(String text_4) {
        Text_4 = text_4;
    }

    public String getText_5() {
        return Text_5;
    }

    public void setText_5(String text_5) {
        Text_5 = text_5;
    }

    public String getText_6() {
        return Text_6;
    }

    public void setText_6(String text_6) {
        Text_6 = text_6;
    }

    public String getText_7() {
        return Text_7;
    }

    public void setText_7(String text_7) {
        Text_7 = text_7;
    }

    public String getText_8() {
        return Text_8;
    }

    public void setText_8(String text_8) {
        Text_8 = text_8;
    }

    public String getText_9() {
        return Text_9;
    }

    public void setText_9(String text_9) {
        Text_9 = text_9;
    }

    public String getText_10() {
        return Text_10;
    }

    public void setText_10(String text_10) {
        Text_10 = text_10;
    }

    public LinkedList<ScientificPaperMetaInformationAuthors> getAuthors() {
        return Authors;
    }

    public void setAuthors(LinkedList<ScientificPaperMetaInformationAuthors> authors) {
        Authors = authors;
    }

    public Timestamp getLocalizedTime() {
        return LocalizedTime;
    }

    public void setLocalizedTime(Timestamp LocalizedTime) {
        this.LocalizedTime = LocalizedTime;
    }
}
