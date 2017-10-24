package org.cc.PersonSearch;

import java.text.ParseException;
import java.util.List;

/**
 * Created by Cristian on 2017-10-24.
 */
public class Playground {


    public static void main(String[] arg) throws ParseException {

        ReadXML readXML = new ReadXML();


       List<Person> listOfPersons = readXML.getPersonList();

       System.out.println("Person objects: " + listOfPersons.size());


        for(Person person : listOfPersons) {

            System.out.println(person.getUID() + " employment periods :" + person.employmentList.size() + " " + "affiliation periods: " + person.affiliationList.size());

        }



    }


}
