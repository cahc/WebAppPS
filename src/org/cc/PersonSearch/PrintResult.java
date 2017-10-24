package org.cc.PersonSearch;


import java.util.*;

/**
 * Created by Cristian on 06/03/16.
 */


public class PrintResult {

    public static class MyComparator implements Comparator<Map.Entry<Object,Comparable>> {

        public int compare(Map.Entry<Object,Comparable> o1,
                           Map.Entry<Object,Comparable> o2) {
            return o2.getValue().compareTo(o1.getValue());
        }

    }

    public static MyComparator myComparator = new MyComparator();


    public static String printHTML(Object p, int index) {

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("<div class=\"panel panel-default\">\n");
        stringBuilder.append("<div class=\"panel-heading\">\n");
        stringBuilder.append("<h4 class=\"panel-title\">\n");
        stringBuilder.append("<a data-toggle=\"collapse\" data-parent=\"#accordion\" href=\"" +"#"+ p.hashCode() + "\">" + ((Person)p).getDisplayName() + "</a>\n");
        stringBuilder.append("</h4>\n");
        stringBuilder.append("</div>\n");
        if(index==0) stringBuilder.append("<div id=\""+ p.hashCode() + "\" class=\"panel-collapse collapse in\">\n");
        if(index!=0) stringBuilder.append("<div id=\""+ p.hashCode() + "\" class=\"panel-collapse collapse\">\n");
        stringBuilder.append(" <div class=\"panel-body\">" +p.toString() +"</div>\n");
        stringBuilder.append("</div>\n");
        stringBuilder.append("</div>\n");


        return stringBuilder.toString();
    }


    public static String printCas(List<Person> list) {

        if(list.size() == 0) {

            return ("<font color=\"red\"><strong>Inga träffar!<strong></font>");


        }


        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<div class=\"panel-group\" id=\"accordion\">\n");

        int counter = 0;
        for(Person p : list) {

            stringBuilder.append( printHTML( p, counter) );
            counter++;
        }

        stringBuilder.append("</div>\n");

        return stringBuilder.toString();
    }


    public static String printCollector(Collector c) {

        if(c.size() == 0) {

            return ("<font color=\"red\"><strong>Inga träffar!<strong></font>");


        }


        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<div class=\"panel-group\" id=\"accordion\">\n");

        Set<Map.Entry<Object,Comparable>> set = c.entrySet();

        List<Map.Entry<Object,Comparable>> list = new ArrayList<>(set);

        Collections.sort(list, myComparator);

        int counter = 0;

        for(Map.Entry e : list) {


            stringBuilder.append( printHTML( e.getKey(), counter) );
            counter++;
        }

        stringBuilder.append("</div>\n");

        return stringBuilder.toString();
    }





}
