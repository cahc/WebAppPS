package org.cc.PersonSearch;

/**
 * Created by crco0001 on 4/26/2016.
 */

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by crco0001 on 4/22/2016.
 */
public class Period implements Comparable<Period> {

    static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private String organisationNumber = "";
    private String OrganisationName = "";
    private String position = "";
    private Date startDate = null; // should not be able to be null?
    private Date endDate = null; // can be null!



    /////////////////////////////////////////////////////

    /* for sorting based on endDate within organisation numbers, not that null endDate is always considered the "latest date" */
    public int compareTo(Period other) {


        int firstCheck = this.getOrganisationNumber().compareTo( other.getOrganisationNumber());

        if(firstCheck == 0) {

            if(this.endDate == null) return 1;
            if(other.endDate == null) return -1;

            return this.endDate.compareTo(other.endDate);
        }


        return firstCheck;


    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Period period = (Period) o;

        if (!organisationNumber.equals(period.organisationNumber)) return false;
        if (!OrganisationName.equals(period.OrganisationName)) return false;
        if (!position.equals(period.position)) return false;
        if (!startDate.equals(period.startDate)) return false;
        return endDate != null ? endDate.equals(period.endDate) : period.endDate == null;

    }

    @Override
    public int hashCode() {
        int result = organisationNumber.hashCode();
        result = 31 * result + OrganisationName.hashCode();
        result = 31 * result + position.hashCode();
        result = 31 * result + startDate.hashCode();
        result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {

        return "<tr><td>"+this.organisationNumber + "</td>" + "<td>"+ this.OrganisationName + "</td>" + "<td>" +this.position + "</td>" + "<td>"+ ( (this.startDate != null) ? simpleDateFormat.format(this.startDate) : "information saknas"  )  + "</td>" + "<td>" + ((this.endDate != null) ? simpleDateFormat.format(this.endDate) : "tillsvidare") +"</td></tr>" ;
    }


    public String printAnställning() {

        return "<tr><td>"+this.organisationNumber + "</td>" + "<td>"+ this.OrganisationName + "</td>" + "<td>" +this.position + "</td>" + "<td>"+ ( (this.startDate != null) ? simpleDateFormat.format(this.startDate) : "information saknas"  )  + "</td>" + "<td>" + ((this.endDate != null) ? simpleDateFormat.format(this.endDate) : "tillsvidare") +"</td><td>Anställd</td></tr>" ;

    }

    public String printAffiliering() {

        return "<tr><td>"+this.organisationNumber + "</td>" + "<td>"+ this.OrganisationName + "</td>" + "<td>" +this.position + "</td>" + "<td>"+ ( (this.startDate != null) ? simpleDateFormat.format(this.startDate) : "information saknas"  )  + "</td>" + "<td>" + ((this.endDate != null) ? simpleDateFormat.format(this.endDate) : "tillsvidare") +"</td><td><font color=\"red\">Anknuten</font></td></tr>" ;

    }

    public String getOrganisationNumber() {
        return organisationNumber;
    }

    public void setOrganisationNumber(String organisationNumber) {
        this.organisationNumber = organisationNumber;
    }

    public String getOrganisationName() {
        return OrganisationName;
    }

    public void setOrganisationName(String organisationName) {
        OrganisationName = organisationName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
