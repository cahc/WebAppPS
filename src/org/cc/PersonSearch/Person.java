package org.cc.PersonSearch;

/**
 * Created by Cristian on 2017-10-24.
 */
/**
 * Created by crco0001 on 4/26/2016.
 */

import java.text.DecimalFormat;
import java.text.Normalizer;
import java.util.*;

import info.debatty.java.stringsimilarity.Cosine;
import info.debatty.java.stringsimilarity.NGram;

/**
 * Created by Cristian on 09/03/16.
 */
public class Person {



    public static Date addDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return cal.getTime();
    }

    final static Cosine ks = new Cosine(2); //N-gram size

    public static String normalizeText(String s) {

        //based on this: https://blog.mafr.de/2015/10/10/normalizing-text-in-java/
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        temp = temp.replaceAll("[^\\p{ASCII}]", "");
        return temp.toLowerCase();
    }

    final static DecimalFormat formatter = new DecimalFormat("#0.000");

    public void mergeEmploymentPeriods() {

        //remove any duplicates if present (why is there duplicates sometimes?)

        for(ListIterator<Period>iterator = employmentList.listIterator(); iterator.hasNext();) {
            Period period = iterator.next();
            if(Collections.frequency(employmentList, period) > 1) {
                iterator.remove();
            }
        }


        //sort by organisation id and enddate and merge

        Collections.sort(this.employmentList, Comparator.reverseOrder());
        List<Period> mergedPeriodList = new ArrayList<Period>();
        int n = this.employmentList.size();

        for(int i=0; i<n;i++) {

            Period p1 = this.employmentList.get(i); // potentially update this with new start date
            i++;


            while(i<n) {

                Period p2 = this.employmentList.get(i);

                if(p1.getOrganisationNumber().equals(p2.getOrganisationNumber()) && p1.getPosition().equals(p2.getPosition()) && addDays(p2.getEndDate(),1).equals(p1.getStartDate())   ) {

                    p1.setStartDate(p2.getStartDate() );
                    i++;
                } else {

                    i--;
                    break;
                }

            }

            mergedPeriodList.add(p1);
        }



        // sort again after enddate only

        Collections.sort(mergedPeriodList, new Comparator<Period>() {
            @Override
            public int compare(Period o1, Period o2) {

                if(o1.getEndDate() == null) return -1;
                if(o2.getEndDate() == null) return 1;

                if(o1.getEndDate().after(o2.getEndDate())) { return -1;}
                if(o1.getEndDate().before(o2.getEndDate())) { return 1;}
                return 0;
            }
        });


        this.employmentList = mergedPeriodList;




    }

    public void mergeAffiliationPeriods() {

        //remove any duplicates if present (why is there duplicates sometimes?)

        for(ListIterator<Period>iterator = affiliationList.listIterator(); iterator.hasNext();) {
            Period period = iterator.next();
            if(Collections.frequency(affiliationList, period) > 1) {
                iterator.remove();
            }
        }


        //sort by organisation id and enddate and merge

        Collections.sort(this.affiliationList, Comparator.reverseOrder());
        List<Period> mergedAffiliationList = new ArrayList<Period>();
        int n = this.affiliationList.size();

        for(int i=0; i<n;i++) {

            Period p1 = this.affiliationList.get(i); // potentially update this with new start date
            i++;


            while(i<n) {

                Period p2 = this.affiliationList.get(i);

                if(p1.getOrganisationNumber().equals(p2.getOrganisationNumber()) && p1.getPosition().equals(p2.getPosition()) && addDays(p2.getEndDate(),1).equals(p1.getStartDate())   ) {

                    p1.setStartDate(p2.getStartDate() );
                    i++;
                } else {

                    i--;
                    break;
                }

            }

            mergedAffiliationList.add(p1);
        }



        // sort again after enddate only

        Collections.sort(mergedAffiliationList, new Comparator<Period>() {
            @Override
            public int compare(Period o1, Period o2) {

                if(o1.getEndDate() == null) return -1;
                if(o2.getEndDate() == null) return 1;

                if(o1.getEndDate().after(o2.getEndDate())) { return -1;}
                if(o1.getEndDate().before(o2.getEndDate())) { return 1;}
                return 0;
            }
        });


        this.affiliationList = mergedAffiliationList;

    }




    String GivenName = "";
    String SurName = "";
    String NormalizedGivenName = "";
    String NormalizedSurName = "";
    String DisplayName = "";
    String NormalizedDisplayName = "";
    String UID = "";
    String Mail = "";

    Map<String, Integer> stringProfile; // for Shingle based similarity matching

    List<Period> employmentList;
    List<Period> affiliationList;

    public String getGivenName() {
        return GivenName;
    }

    public void setGivenName(String givenName) {
        GivenName = givenName;
        NormalizedGivenName = normalizeText(givenName);
    }

    public String getSurName() {
        return SurName;
    }

    public String getNormalizedSurName() {return  NormalizedSurName;}

    public String getNormalizedGivenName() {return NormalizedGivenName;}

    public void setSurName(String surName) {
        SurName = surName;
        NormalizedSurName = normalizeText(surName);
    }

    public String getDisplayName() {
        return DisplayName;
    }

    public void setDisplayName(String displayName) {
        DisplayName = displayName;


        stringProfile = ks.getProfile( normalizeText(displayName) );
    }

    public void setNormalizedDisplayName(String displayName) {

        NormalizedDisplayName = normalizeText(displayName);

    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getMail() {
        return Mail;
    }

    public void setMail(String mail) {
        Mail = mail;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;

        if (!GivenName.equals(person.GivenName)) return false;
        if (!SurName.equals(person.SurName)) return false;
        if (!DisplayName.equals(person.DisplayName)) return false;
        if (!UID.equals(person.UID)) return false;
        return Mail.equals(person.Mail);

    }

    @Override
    public int hashCode() {
        int result = GivenName.hashCode();
        result = 31 * result + SurName.hashCode();
        result = 31 * result + DisplayName.hashCode();
        result = 31 * result + UID.hashCode();
        result = 31 * result + Mail.hashCode();
        return result;
    }

    @Override
    public String toString() {



        StringBuilder stringBuilderEmploymentsAndAffiliations = new StringBuilder(200);


        stringBuilderEmploymentsAndAffiliations.append("<table class=\"table\">");
        stringBuilderEmploymentsAndAffiliations.append("<thead>");
        stringBuilderEmploymentsAndAffiliations.append("<tr><th>Organisationsnummer</th><th>Organisationsnamn</th><th>Befattning</th><th>Startdatum</th><th>Slutdatum</th><th>Typ</th></tr>");
        stringBuilderEmploymentsAndAffiliations.append("</thead>");
        stringBuilderEmploymentsAndAffiliations.append("<tbody>");

        for (Period p : employmentList) {

            stringBuilderEmploymentsAndAffiliations.append(p.printAnställning());

        }

        for(Period p : affiliationList) {

            stringBuilderEmploymentsAndAffiliations.append(p.printAffiliering());
        }

        stringBuilderEmploymentsAndAffiliations.append("</tbody>");
        stringBuilderEmploymentsAndAffiliations.append("</table>");


        return "<p>"+ this.GivenName + " " + this.SurName + " <u>" + this.UID + "</u> " + this.Mail + "</p>" + stringBuilderEmploymentsAndAffiliations.toString();




    }


}
