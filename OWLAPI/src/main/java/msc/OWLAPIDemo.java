package msc;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Optional;


import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.FunctionalSyntaxDocumentFormat;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;

public class OWLAPIDemo {
	public static void main(String[] args) {
		String oldOntologyFilename = "/home/tom/Documents/Uliege/SecondSemester/Semantic Data/Projects/Olympic-games-ontology/olympics-gamesXML.owl";
		String newOntologyFilename = "/home/tom/Documents/Uliege/SecondSemester/Semantic Data/Projects/Olympic-games-ontology/olympics-games_modifiedXML.owl";
		Optional<IRI> oldontologyIRI;
		OWLOntologyManager manager;
		OWLDataFactory dataFactory;
		OWLOntology oldOntology;

		try {
			File fileIN = new File(oldOntologyFilename);	
			File fileOUT = new File(newOntologyFilename);
			manager = OWLManager.createOWLOntologyManager();
			
			
			// Load ontology from file
			System.out.println("");
			System.out.println("=========================LOADING ONTOLOGY========================");

			oldOntology = manager.loadOntologyFromOntologyDocument(fileIN);		
			System.out.println(oldOntology);
			
			oldontologyIRI = oldOntology.getOntologyID().getOntologyIRI();
			dataFactory = manager.getOWLDataFactory();
			
			// Create hermit reasoner with the OWLReasoner interface
			OWLReasoner reasoner = new Reasoner.ReasonerFactory().createNonBufferingReasoner(oldOntology);
			reasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY);
			
			/*
			 * 
			 * ADD A SPORTCOMPETITION TO THE ONTOLOGY
			 * 
			 */
			System.out.println("");
			System.out.println("================================================================");
			System.out.println("==============ADD A SPORTCOMPETITION TO THE ONTOLOGY============");
			System.out.println("================================================================");
			System.out.println("");
			
			// Get instances from SportCompetition and its subclasses
			System.out.println("\nInstances of SportCompetition BEFORE addition to the Ontology :\n");
			reasoner.getInstances(dataFactory.getOWLClass(IRI.create(oldontologyIRI.get() + "#SportCompetition")),false)
					.forEach(System.out::println);
			

		    
		    // Get the relevant classes stores in the loaded ontology
		    OWLClass unitCompetitionClass = dataFactory.getOWLClass(IRI.create(oldontologyIRI.get() + "#UnitCompetition"));

		    // Get object properties stored within the loaded ontology.
		    OWLObjectPropertyExpression hasSport = dataFactory.getOWLObjectProperty(
		    		IRI.create(oldontologyIRI + "#hasSport"));
		    OWLObjectPropertyExpression hasHost = dataFactory.getOWLObjectProperty(
		    		IRI.create(oldontologyIRI + "#hasHost"));
		    
		    // Get two property values stored within the loaded ontology.
		    OWLNamedIndividual sochi2014Individual= dataFactory.getOWLNamedIndividual(
		    		IRI.create(oldontologyIRI.get() + "#Sochi2014"));
		    OWLNamedIndividual biathlonIndividualIndividual = dataFactory.getOWLNamedIndividual(
		    		IRI.create(oldontologyIRI.get() + "#Biathlon_Individual"));

		    // Create the new individual : a Biathlon competition during Sochi 2014
		    String wholename = oldontologyIRI.get() + "#Mens_Individual_Biathlon_XXII_Winter Olympiad";
            OWLNamedIndividual biathlonCompetition = dataFactory.getOWLNamedIndividual(IRI.create(wholename));

            // biathlonCompetition is of type UnitCompetition
            OWLClassAssertionAxiom isOfType = dataFactory.getOWLClassAssertionAxiom(unitCompetitionClass, biathlonCompetition);
            manager.addAxiom(oldOntology, isOfType);
		    
            // biathlonCompetition :hasSport Biathlon_Individual(name of the Sport)
            OWLObjectPropertyAssertionAxiom biathlonCompetitionHasSport = dataFactory.getOWLObjectPropertyAssertionAxiom(
            		hasSport, biathlonCompetition, biathlonIndividualIndividual);
            manager.addAxiom(oldOntology, biathlonCompetitionHasSport);
            
            // biathlonCompetition :hasHost Sochi2014
            OWLObjectPropertyAssertionAxiom biathlonCompetitionHasHost = dataFactory.getOWLObjectPropertyAssertionAxiom(
            		hasHost, biathlonCompetition, sochi2014Individual);
            manager.addAxiom(oldOntology, biathlonCompetitionHasHost);
            
            OWLDocumentFormat format = manager.getOntologyFormat(oldOntology);
            manager.saveOntology(oldOntology, format, IRI.create(fileOUT.toURI()));
			System.out.println("=========================SAVING ONTOLOGY========================");
			System.out.println(oldOntology);
			
			// Get instances from SportCompetition and its subclasses
			System.out.println("\nInstances of SportCompetition AFTER addition of "
					+ "#Mens_Individual_Biathlon_XXII_Winter Olympiad to the Ontology : \n");
			reasoner.getInstances(dataFactory.getOWLClass(IRI.create(oldontologyIRI.get() + "#SportCompetition")),false)
					.forEach(System.out::println);
			
			System.out.println("");
			System.out.println("================================================================");
			System.out.println("===================== OTHER REASONING ==========================");
			System.out.println("================================================================");
			System.out.println("");

			/*
			 * 
			 * REASONER
			 * 
			 */
			

			// Get subclasses of Sport
			System.out.println("\nSubclasses of Sport :");
			reasoner.getSubClasses(dataFactory.getOWLClass(IRI.create(oldontologyIRI.get() + "#Sport")), false)
					.forEach(System.out::println);
			
			// Get subclasses of SportCompetition
			System.out.println("\nSubclasses of SportCompetition :");
			reasoner.getSubClasses(dataFactory.getOWLClass(IRI.create(oldontologyIRI.get() + "#SportCompetition")), false)
					.forEach(System.out::println);
			
	
			// Get instances from Sport and its subclasses
			System.out.println("\nInstances of Sport :");
			reasoner.getInstances(dataFactory.getOWLClass(IRI.create(oldontologyIRI.get() + "#Sport")),false)
					.forEach(System.out::println);
		 } catch (Exception e) {
			 e.printStackTrace();
 		}
		
	}
}
