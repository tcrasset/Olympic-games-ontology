package msc;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.expression.OWLEntityChecker;
import org.semanticweb.owlapi.expression.ParserException;
import org.semanticweb.owlapi.expression.ShortFormEntityChecker;
import org.semanticweb.owlapi.formats.FunctionalSyntaxDocumentFormat;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.util.ShortFormProvider;
import org.semanticweb.owlapi.util.SimpleShortFormProvider;
import org.coode.owlapi.manchesterowlsyntax.ManchesterOWLSyntaxEditorParser;
import org.semanticweb.owlapi.util.BidirectionalShortFormProvider;
import org.semanticweb.owlapi.util.BidirectionalShortFormProviderAdapter;
import org.semanticweb.owlapi.util.ShortFormProvider;
import org.semanticweb.owlapi.util.SimpleShortFormProvider;

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
			
			System.out.println("");
			System.out.println("========================================================================");
			System.out.println("===================== MANCHESTER SYNTAX QUERIES==========================");
			System.out.println("========================================================================");
			System.out.println("");
			
	        ShortFormProvider shortFormProvider = new SimpleShortFormProvider();
	        // Create the DLQueryPrinter helper class. This will manage the
	        // parsing of input and printing of results
	        DLQueryPrinter dlQueryPrinter = new DLQueryPrinter(new DLQueryEngine(reasoner,
	                shortFormProvider), shortFormProvider);
	        
            dlQueryPrinter.askQuery("hasSport some Sport");

			
			
		 } catch (Exception e) {
			 e.printStackTrace();
 		}
		
	}
}
























class DLQueryEngine {
    private final OWLReasoner reasoner;
    private final DLQueryParser parser;

    public DLQueryEngine(OWLReasoner reasoner, ShortFormProvider shortFormProvider) {
        this.reasoner = reasoner;
        parser = new DLQueryParser(reasoner.getRootOntology(), shortFormProvider);
    }

    public Set<OWLClass> getSuperClasses(String classExpressionString, boolean direct) {
        if (classExpressionString.trim().length() == 0) {
            return Collections.emptySet();
        }
        OWLClassExpression classExpression = parser
                .parseClassExpression(classExpressionString);
        NodeSet<OWLClass> superClasses = reasoner
                .getSuperClasses(classExpression, direct);
        return superClasses.getFlattened();
    }

    public Set<OWLClass> getEquivalentClasses(String classExpressionString) {
        if (classExpressionString.trim().length() == 0) {
            return Collections.emptySet();
        }
        OWLClassExpression classExpression = parser
                .parseClassExpression(classExpressionString);
        Node<OWLClass> equivalentClasses = reasoner.getEquivalentClasses(classExpression);
        Set<OWLClass> result = null;
        if (classExpression.isAnonymous()) {
            result = equivalentClasses.getEntities();
        } else {
            result = equivalentClasses.getEntitiesMinus(classExpression.asOWLClass());
        }
        return result;
        }

    public Set<OWLClass> getSubClasses(String classExpressionString, boolean direct) {
        if (classExpressionString.trim().length() == 0) {
            return Collections.emptySet();
        }
        OWLClassExpression classExpression = parser
                .parseClassExpression(classExpressionString);
        NodeSet<OWLClass> subClasses = reasoner.getSubClasses(classExpression, direct);
        return subClasses.getFlattened();
        }

    public Set<OWLNamedIndividual> getInstances(String classExpressionString,
            boolean direct) {
        if (classExpressionString.trim().length() == 0) {
            return Collections.emptySet();
        }
        OWLClassExpression classExpression = parser
                .parseClassExpression(classExpressionString);
        NodeSet<OWLNamedIndividual> individuals = reasoner.getInstances(classExpression,
                direct);
        return individuals.getFlattened();
        }
    }

class DLQueryParser {
    private final OWLOntology rootOntology;
    private final BidirectionalShortFormProvider bidiShortFormProvider;

    public DLQueryParser(OWLOntology rootOntology, ShortFormProvider shortFormProvider) {
        this.rootOntology = rootOntology;
        OWLOntologyManager manager = rootOntology.getOWLOntologyManager();
        Set<OWLOntology> importsClosure = rootOntology.getImportsClosure();
        // Create a bidirectional short form provider to do the actual mapping.
        // It will generate names using the input
        // short form provider.
        bidiShortFormProvider = new BidirectionalShortFormProviderAdapter(manager,
                importsClosure, shortFormProvider);
    }

    public OWLClassExpression parseClassExpression(String classExpressionString) {
        OWLDataFactory dataFactory = rootOntology.getOWLOntologyManager()
                .getOWLDataFactory();
        ManchesterOWLSyntaxEditorParser parser = new ManchesterOWLSyntaxEditorParser(
                dataFactory, classExpressionString);
        parser.setDefaultOntology(rootOntology);
        OWLEntityChecker entityChecker = new ShortFormEntityChecker(bidiShortFormProvider);
        parser.setOWLEntityChecker(entityChecker);
        return parser.parseClassExpression();
        }
    }

class DLQueryPrinter {
    private final DLQueryEngine dlQueryEngine;
    private final ShortFormProvider shortFormProvider;

    public DLQueryPrinter(DLQueryEngine engine, ShortFormProvider shortFormProvider) {
        this.shortFormProvider = shortFormProvider;
        dlQueryEngine = engine;
        }

    public void askQuery(String classExpression) {
        if (classExpression.length() == 0) {
            System.out.println("No class expression specified");
        } else {
            try {
                StringBuilder sb = new StringBuilder();
                sb.append("\nQUERY:   ").append(classExpression).append("\n\n");
                Set<OWLClass> superClasses = dlQueryEngine.getSuperClasses(
                        classExpression, false);
                printEntities("SuperClasses", superClasses, sb);
                Set<OWLClass> equivalentClasses = dlQueryEngine
                        .getEquivalentClasses(classExpression);
                printEntities("EquivalentClasses", equivalentClasses, sb);
                Set<OWLClass> subClasses = dlQueryEngine.getSubClasses(classExpression,
                        true);
                printEntities("SubClasses", subClasses, sb);
                Set<OWLNamedIndividual> individuals = dlQueryEngine.getInstances(
                        classExpression, true);
                printEntities("Instances", individuals, sb);
                System.out.println(sb.toString());
            } catch (ParserException e) {
                System.out.println(e.getMessage());
            }
            }
        }

    private void printEntities(String name, Set<? extends OWLEntity> entities,
            StringBuilder sb) {
        sb.append(name);
        int length = 50 - name.length();
        for (int i = 0; i < length; i++) {
            sb.append(".");
        }
        sb.append("\n\n");
        if (!entities.isEmpty()) {
            for (OWLEntity entity : entities) {
                sb.append("\t").append(shortFormProvider.getShortForm(entity))
                        .append("\n");
            }
        } else {
            sb.append("\t[NONE]\n");
            }
        sb.append("\n");
        }
    }	
