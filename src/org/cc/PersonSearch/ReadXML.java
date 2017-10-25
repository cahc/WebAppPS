package org.cc.PersonSearch;
/**
 * Created by crco0001 on 4/26/2016.
 */

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Cristian on 21/03/16.
 */
public class ReadXML {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private List<Person> personList = new ArrayList<>();
    private String updated = "-99";
    final SimpleDateFormat simpleDateParser = new SimpleDateFormat("yyyyMMddHHmmSSS");

    private final String osName = System.getProperty("os.name");


    public ReadXML() throws ParseException {


        boolean isWinDev = false;
        if(osName != null && osName.toLowerCase().contains("windows") )   isWinDev = true;

        File dir = null;
        if(isWinDev) {

            dir = new File("C:\\mnt\\pdata");
        } else{

             dir = new File("/mnt/pdata");
        }


        //write to catalina log
        System.out.println("directory with pdata exists?: " + dir.exists());

        FilenameFilter filenameFilter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.matches("^Personal.*?\\d{10}.*?.xml$");
            }
        };

        File[] files = dir.listFiles(filenameFilter);
        Arrays.sort(files, Collections.reverseOrder());


        File personDataXml = files[0]; //latest file


        //write to catalina log

        System.out.println("latest file: " + personDataXml.toString() );

        Pattern p = Pattern.compile("\\d{8,15}");

        Matcher m = p.matcher(personDataXml.toString());

        if (m.find()) {

            try {
                updated = simpleDateFormat.format( simpleDateParser.parse(m.group(0))).toString();
            } catch (ParseException e) {

            }

        }



        XMLInputFactory factory = XMLInputFactory.newInstance();


        try {
            XMLEventReader eventReader = factory.createXMLEventReader(new FileInputStream(personDataXml));

            while (eventReader.hasNext()) {

                XMLEvent event = eventReader.nextEvent();


                if (event.getEventType() == XMLStreamConstants.START_ELEMENT) {
                    StartElement startElement = event.asStartElement();

                    if ("Employee".equals(startElement.getName().getLocalPart())) {

                        //A new person object in XML
                        Person newPerson = new Person();


                        //Get the attributes
                        Attribute attribute = startElement.getAttributeByName(new QName("GivenName"));
                        newPerson.setGivenName(attribute.getValue());
                        attribute = startElement.getAttributeByName(new QName("SurName"));
                        newPerson.setSurName(attribute.getValue());
                        attribute = startElement.getAttributeByName(new QName("DisplayName"));
                        newPerson.setDisplayName(attribute.getValue());
                        newPerson.setNormalizedDisplayName(  attribute.getValue() );
                        attribute = startElement.getAttributeByName(new QName("UID"));
                        newPerson.setUID(attribute.getValue().toLowerCase().trim());
                        attribute = startElement.getAttributeByName(new QName("Mail"));
                        newPerson.setMail(attribute.getValue());

                        //get the period data

                        List<Period> employments = new ArrayList<>();
                        List<Period> affiliations = new ArrayList<>();


                        while(true) { //loop over all elements for an employee

                            event = eventReader.nextEvent();

                            if(event.getEventType() == XMLStreamConstants.START_ELEMENT && event.asStartElement().getName().getLocalPart().equals("EmploymentPeriods")) {

                                while(true) {

                                    event = eventReader.nextEvent();

                                    if(event.isStartElement() && event.asStartElement().getName().getLocalPart().equals("Period") ) {
                                        startElement = event.asStartElement();
                                        Period period = new Period();
                                        String organisationNummer = startElement.getAttributeByName( new QName( "OrganisationNumber" )).getValue() ;
                                        String organisationNamn   = startElement.getAttributeByName( new QName( "OrganisationName" )).getValue();
                                        String position =   startElement.getAttributeByName( new QName( "Position" )).getValue();String startDatum =  startElement.getAttributeByName( new QName( "StartDate" )).getValue();
                                        String stopDatum =   startElement.getAttributeByName( new QName( "StopDate" )).getValue();
                                        period.setStartDate( simpleDateFormat.parse( startDatum )  );
                                        period.setPosition( position );
                                        if(organisationNamn.equals("")) organisationNamn = "information saknas";
                                        period.setOrganisationName( organisationNamn );
                                        period.setOrganisationNumber( organisationNummer );
                                        if(stopDatum.equals("")) {

                                            //do nothing, keep as null
                                        } else {

                                            period.setEndDate( simpleDateFormat.parse( stopDatum ) );
                                        }

                                        employments.add(period);
                                    }

                                    if(event.isEndElement() && event.asEndElement().getName().getLocalPart().equals("EmploymentPeriods")) break;

                                }


                            } //employment processing loop

                            if(event.getEventType() == XMLStreamConstants.START_ELEMENT && event.asStartElement().getName().getLocalPart().equals("AffiliationPeriods")) {

                                while(true) {

                                    event = eventReader.nextEvent();

                                    if(event.isStartElement() && event.asStartElement().getName().getLocalPart().equals("Period") ) {
                                        startElement = event.asStartElement();
                                        Period period = new Period();
                                        String organisationNummer = startElement.getAttributeByName( new QName( "OrganisationNumber" )).getValue() ;
                                        String organisationNamn   = startElement.getAttributeByName( new QName( "OrganisationName" )).getValue();
                                        String position =   startElement.getAttributeByName( new QName( "Position" )).getValue();String startDatum =  startElement.getAttributeByName( new QName( "StartDate" )).getValue();
                                        String stopDatum =   startElement.getAttributeByName( new QName( "StopDate" )).getValue();
                                        period.setStartDate( simpleDateFormat.parse( startDatum )  );
                                        period.setPosition( position );
                                        if(organisationNamn.equals("")) organisationNamn = "information saknas";
                                        period.setOrganisationName( organisationNamn );
                                        period.setOrganisationNumber( organisationNummer );
                                        if(stopDatum.equals("")) {

                                            //do nothing, keep as null
                                        } else {

                                            period.setEndDate( simpleDateFormat.parse( stopDatum ) );
                                        }

                                        affiliations.add(period);
                                    }

                                    if(event.isEndElement() && event.asEndElement().getName().getLocalPart().equals("AffiliationPeriods")) break;

                                }


                            } //affiliation processing loop


                            if(event.getEventType() == XMLStreamConstants.END_ELEMENT) {

                                if(event.asEndElement().getName().getLocalPart().equals("Employee")) {

                                    //Collections.sort(employmentList,Comparator.reverseOrder());
                                    newPerson.employmentList = employments;
                                    newPerson.affiliationList = affiliations;
                                    newPerson.mergeEmploymentPeriods(); //sort and merge!
                                    newPerson.mergeAffiliationPeriods(); //sort and merge!
                                    personList.add(newPerson);
                                    break;
                                }
                            }


                        } //while true ends (after we encountered a Employee end tag)



                    } //if Employee


                }


            } //while XML has next


            eventReader.close();

        } catch (XMLStreamException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }




    }

    public List<Person> getPersonList() {

        return personList;
    }

    public int getNumberOfPersons() {

        return personList.size();
    }

    public String getUpdated(){

        return updated;
    }

}

