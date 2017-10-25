package org.cc.PersonSearch;

import info.debatty.java.stringsimilarity.Cosine;
import info.debatty.java.stringsimilarity.NormalizedLevenshtein;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Cristian on 2017-10-24.
 */
@WebServlet(name = "SearchServlet", urlPatterns = {"/search"})

public class SearchServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ScheduledExecutorService backgroundExecutor;
    private final AtomicReference<List<Person>> cachedPersons = new AtomicReference<List<Person>>();
    private final AtomicReference<String> updated = new AtomicReference<String>();




    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**

     */
    public void init(ServletConfig config) throws ServletException {
        //initialize to empty
        updated.set("null");
        List<Person> emptyList =  new ArrayList<>();
        cachedPersons.set(emptyList);

        backgroundExecutor = Executors.newSingleThreadScheduledExecutor();
        backgroundExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {

                ReadXML readXML = null;
                try {
                    readXML = new ReadXML();
                } catch (Throwable t) {
                    t.printStackTrace();
                    System.out.println("ReadXML throwed an exception");
                }


                if(readXML != null)  {

                    cachedPersons.set(readXML.getPersonList());
                    updated.set(readXML.getUpdated());

                } else {

                    System.out.println("readXML failed!!");
                }

            }
        }, 0, 12, TimeUnit.HOURS);




    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("cashed person object is null? " + (this.cachedPersons == null)); //debug print
        System.out.println("request: " + request.getQueryString()); //debug print
        String status = request.getParameter("serverstatus"); 

        if(status != null) {
            response.setCharacterEncoding("UTF-8");
            PrintWriter out=response.getWriter();

          //  String resp = "<span class=\"badge\"><div id=\"nrobj\">" +this.cachedPersons.get().size() + "</div></span> personobjekt uppdaterade <span class=\"badge\"><div id=\"updated\">" + this.updated.get() + "</div></span>";
           // String resp = "  <span class=\"badge\"><div id=\"nrobj\">" +this.cachedPersons.get().size() + "</div></span> personobjekt uppdaterade <span class=\"badge\"><div id=\"updated\">" + this.updated.get() + "</div></span>";

            String resp = "Databasen uppdaterades <span class=\"badge\"><div id=\"updated\">" + this.updated.get() + "</div></span>" + " och innehåller " + "<span class=\"badge\"><div id=\"nrobj\">" +this.cachedPersons.get().size() + "</div></span>" + " personobjekt" ;
            out.print(resp);
            out.flush();
            out.close();
            return;
        }

        String name=request.getParameter("name");
        String cas=request.getParameter("cas");
        String algo = request.getParameter("algo");
        String maxhits = request.getParameter("maxhits");

        response.setCharacterEncoding("UTF-8");
        PrintWriter out=response.getWriter();

        if(name.length() == 0 && cas.length() == 0 ) {

            out.println("<font color=\"red\"><strong>Ogiltig sökfråga!<strong></font>");
            out.flush();
            out.close();
        } else if(cas.length() != 0) {

            //use exact CAS search
            ArrayList<Person> casList = new ArrayList<>(5);
            cas = cas.toLowerCase().trim();

            for(int i=0; i<cachedPersons.get().size(); i++) {

                Person p = cachedPersons.get().get(i);

                if(cas.equals( p.getUID()  )) {

                    casList.add(p);

                }

            }
            out.println( PrintResult.printCas(casList) ) ;
            out.flush();
            out.close();
        }

        else {

            //USE FUZZY SEARCH OR EXAKT..

            Map<String,Integer> nameQueryProfile = Person.ks.getProfile( Person.normalizeText(name) );
            
            String normalizedNameQuery = Person.normalizeText(name);
            String[] initialEfternamn = normalizedNameQuery.split(" ");


            int topN = Integer.valueOf(maxhits);

            Collector<Person,Double> personCollector = new Collector<Person,Double>(topN); //  top 10 hits


            for(int i=0; i< cachedPersons.get().size(); i++) {

                Person p = cachedPersons.get().get(i);
                Double sim = -1.0;
                if("N-gram".equals(algo)) try {


                    Cosine cosine = new Cosine();

                    sim = cosine.similarity(nameQueryProfile, p.stringProfile);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                if("Levenshtein".equals(algo)) {
                    NormalizedLevenshtein levenshtein = new NormalizedLevenshtein();

                    sim = levenshtein.similarity(normalizedNameQuery,p.NormalizedDisplayName   );
                }

                if("exakt".equals(algo)) {

                    if(initialEfternamn.length != 2)  {

                        out.println("<font color=\"red\"><strong>Ogiltig sökfråga!<strong></font>");
                        out.flush();
                        out.close();
                        return;
                    }


                    if(p.getNormalizedGivenName().startsWith(initialEfternamn[0]) && p.getNormalizedSurName().startsWith( initialEfternamn[1] )) {

                        sim = 1.0;
                    }

                }


                if(sim >= 0.4) { // not meaningful to show hits with lower similarity

                    personCollector.offer(p,sim);

                }


            }


            out.println( PrintResult.printCollector( personCollector ) ) ;
            out.flush();
            out.close();

        }
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //TODO implement if necessary
    }

}