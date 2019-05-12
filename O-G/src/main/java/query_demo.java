import java.io.*;


import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.query.Syntax;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.query.ResultSet;

public class query_demo {

	public static void main(String[] args) throws IOException {
		// Open the bloggers RDF graph from the filesystem
		InputStream in = new FileInputStream(new File("infered-olympic-games.rdf"));
		String IRI = "http://www.semanticweb.org/antoinelouis/ontologies/2019/4/olympic-games#";
		// Create an empty in‑memory model and populate it from the graph
		Model model = ModelFactory.createMemModelMaker().createModel("Model");
		model.read(in,null); // null base URI, since model URIs are absolute
		in.close();
		
		query_model(model, "100MSprint_Rules.rq");
		query_model(model, "Athlete_Multiple_Medal.rq");
		query_model(model, "HeightPhelps.rq");
		query_model(model, "nationality.rq");
		query_model(model, "nb_gold_USA_London.rq");
		query_model(model, "Sport_Rio.rq");
		query_model(model, "Winner_2012.rq");
		query_model(model, "winner_Tennis_2012.rq");
		
		// Create a new query
		query_model(model, "nb_medal_bolt.rq");
		
		// Add a individual
		Resource sprintRio = model.createResource(IRI + "Men's_100M_Sprint_XXXI_Olympiad");
		sprintRio.addProperty(RDF.type, model.getResource(IRI+"MultiStageCompetition"));
		sprintRio.addProperty(model.getProperty(IRI+"hasAwardedSecondPlaceTo"), model.getResource(IRI+"UsainBolt"));
		sprintRio.addProperty(model.getProperty(IRI+"hasCompetitor"), model.getResource(IRI+"UsainBolt"));
		sprintRio.addProperty(model.getProperty(IRI+"hasSport"), model.getResource(IRI+"100M_Sprint"));
		sprintRio.addProperty(model.getProperty(IRI+"hasHost"), model.getResource(IRI+"London2012"));
		sprintRio.addProperty(model.getProperty(IRI+"hasCompetitorGender"), "Male");
		// Do the same query
		query_model(model, "nb_medal_bolt.rq");
		
		// Modify an properties by deleting the old one
		// and add a new one
		//Query the podium of 200M sprint of London 2012
		query_model(model, "podium_200M_London.rq");
		// Change the first and second place
		Resource sprintLondon = model.getResource(IRI + "Men's_200M_Sprint_XXX_Olympiad");
		sprintLondon.removeAll(model.getProperty(IRI+"hasAwardedFirstPlaceTo"));
		sprintLondon.removeAll(model.getProperty(IRI+"hasAwardedSecondPlaceTo"));
		sprintLondon.addProperty(model.getProperty(IRI+"hasAwardedFirstPlaceTo"), model.getResource(IRI+"YohanBlake"));
		sprintLondon.addProperty(model.getProperty(IRI+"hasAwardedSecondPlaceTo"), model.getResource(IRI+"UsainBolt"));
		//Do the same query
		query_model(model, "podium_200M_London.rq");
		
		
		//Remove the Men's 100M Sprint of London 2012
		query_model(model, "Winner_100M_2012.rq");
		Resource sprintLondon100 = model.createResource(IRI + "Men's_100M_Sprint_XXX_Olympiad");
		//Remove all the statements where the resource is the subject
		model.removeAll(sprintLondon100, null, null);
		//Remove all the statements where the resource is the object
		model.removeAll(null, null, sprintLondon100);
		query_model(model, "Winner_100M_2012.rq");
		
		
		query_model(model, "nb_medal_bolt.rq");
		
		OutputStream out = new FileOutputStream(new File("infered-olympic-games-rewrited.rdf"));
		model.write(out, "RDF/XML");
		out.close();
		
	}
	
	public static void query_model(Model model, String query_file) throws IOException{
		//Read the query in the textfile
		FileReader fileReader = new FileReader(query_file);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		String queryString = "";
		String line = null;
		while((line = bufferedReader.readLine()) != null) {
            queryString += line;
        }   
        bufferedReader.close();  
       
        //Create the query
		Query query = QueryFactory.create(queryString, Syntax.syntaxARQ);

		// Execute the query and obtain results
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect();

		// Output query results    
		ResultSetFormatter.out(System.out, results, query);
		
		// Important ‑ free up resources used running the query
		qe.close();
	}
	
	

}
