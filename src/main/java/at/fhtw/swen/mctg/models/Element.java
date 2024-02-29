package at.fhtw.swen.mctg.models;

public class Element {
    int elementID;
    String elementName;
    int vulnerability;

    public Element(int elementID, String elementName, int vulnerability) {
        this.elementID = elementID;
        this.elementName = elementName;
        this.vulnerability = vulnerability;
    }
}
