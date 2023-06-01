package at.kulinz.jaegerstaetter.frontendmodel.dtoobj;

public enum IndexSortedField {

    TITLE(IndexFieldname.RESOURCE_TITLE),
    DATING(IndexFieldname.DATING),
    RESOURCE_ID(IndexFieldname.RESOURCE_ID);

    public final String fieldname;

    public final IndexFieldname mainField;

    IndexSortedField(IndexFieldname fn) {
        this.mainField = fn;
        this.fieldname = fn.fieldname + "_sort";
    }

}
