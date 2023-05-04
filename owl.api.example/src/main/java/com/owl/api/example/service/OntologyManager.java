package com.owl.api.example.service;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.TurtleDocumentFormat;
import org.semanticweb.owlapi.model.*;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OntologyManager {

    public OWLOntology getOntology() {
        return ontology;
    }
    private OWLOntology ontology;
    public OntologyManager() throws OWLOntologyCreationException, OWLOntologyStorageException {
        OWLOntologyManager man = OWLManager.createOWLOntologyManager();

        //OWLOntology ontology = man.loadOntologyFromOntologyDocument(new File("data/knowledge-base.owl"));
        //saveOntologyIntoSeparateFiles(man, ontology);

        OWLOntology holjaIndividualsOntology = man.loadOntologyFromOntologyDocument(new File("data/HoljaIndividualOntology.owl"));
        OWLOntology kataIndividualsOntology = man.loadOntologyFromOntologyDocument(new File("data/KataIndividualOntology.owl"));
        OWLOntology shoneIndividualsOntology = man.loadOntologyFromOntologyDocument(new File("data/ShoneIndividualOntology.owl"));

        OWLOntology holjaClassesOntology = man.loadOntologyFromOntologyDocument(new File("data/HoljaClassOntology.owl"));
        OWLOntology kataClassesOntology = man.loadOntologyFromOntologyDocument(new File("data/KataClassOntology.owl"));
        OWLOntology shoneClassesOntology = man.loadOntologyFromOntologyDocument(new File("data/ShoneClassOntology.owl"));

        OWLOntology holjaObjectPropertiesOntology = man.loadOntologyFromOntologyDocument(new File("data/HoljaObjectPropertyOntology.owl"));
        OWLOntology kataObjectPropertiesOntology = man.loadOntologyFromOntologyDocument(new File("data/KataObjectPropertyOntology.owl"));
        OWLOntology shoneObjectPropertiesOntology = man.loadOntologyFromOntologyDocument(new File("data/ShoneObjectPropertyOntology.owl"));

        OWLOntology holjaDataPropertiesOntology = man.loadOntologyFromOntologyDocument(new File("data/HoljaDataPropertyOntology.owl"));
        OWLOntology kataDataPropertiesOntology = man.loadOntologyFromOntologyDocument(new File("data/KataDataPropertyOntology.owl"));
        OWLOntology shoneDataPropertiesOntology = man.loadOntologyFromOntologyDocument(new File("data/ShoneDataPropertyOntology.owl"));

        OWLOntology combinedOntology = man.createOntology();

        man.addAxioms(combinedOntology, holjaIndividualsOntology.getAxioms());
        man.addAxioms(combinedOntology, kataIndividualsOntology.getAxioms());
        man.addAxioms(combinedOntology, shoneIndividualsOntology.getAxioms());

        man.addAxioms(combinedOntology, holjaClassesOntology.getAxioms());
        man.addAxioms(combinedOntology, kataClassesOntology.getAxioms());
        man.addAxioms(combinedOntology, shoneClassesOntology.getAxioms());

        man.addAxioms(combinedOntology, holjaObjectPropertiesOntology.getAxioms());
        man.addAxioms(combinedOntology, kataObjectPropertiesOntology.getAxioms());
        man.addAxioms(combinedOntology, shoneObjectPropertiesOntology.getAxioms());

        man.addAxioms(combinedOntology, holjaDataPropertiesOntology.getAxioms());
        man.addAxioms(combinedOntology, kataDataPropertiesOntology.getAxioms());
        man.addAxioms(combinedOntology, shoneDataPropertiesOntology.getAxioms());

        Set<OWLClass> classes = combinedOntology.getClassesInSignature();
        Set<OWLObjectProperty> objectProperties = combinedOntology.getObjectPropertiesInSignature();
        Set<OWLDataProperty> dataProperties = combinedOntology.getDataPropertiesInSignature();
        Set<OWLNamedIndividual> individuals = combinedOntology.getIndividualsInSignature();

        System.out.println("\nTotal number of classes: " + classes.size());
        System.out.println("Total number of object properties: " + objectProperties.size());
        System.out.println("Total number of data properties: " + dataProperties.size());
        System.out.println("Total number of individuals: " + individuals.size() + "\n");

        this.ontology = combinedOntology;
    }

    private static void saveOntologyIntoSeparateFiles(OWLOntologyManager man, OWLOntology ontology) throws OWLOntologyCreationException, OWLOntologyStorageException {
        Set<OWLAxiom> allAxioms = ontology.getAxioms();
        TurtleDocumentFormat turtleFormat = new TurtleDocumentFormat();

        Set<OWLClassAxiom> classAxioms = allAxioms.stream()
                .filter(axiom -> axiom instanceof OWLClassAxiom)
                .map(axiom -> (OWLClassAxiom) axiom)
                .collect(Collectors.toSet());
        OWLOntology classOntology = man.createOntology();
        classAxioms.stream().forEach(classOntology::addAxiom);
        man.saveOntology(classOntology, turtleFormat, IRI.create(new File("data/classOntology.owl")));

        Set<OWLObjectPropertyAxiom> objectPropertyAxioms = allAxioms.stream()
                .filter(axiom -> axiom instanceof OWLObjectPropertyAxiom)
                .map(axiom -> (OWLObjectPropertyAxiom) axiom)
                .collect(Collectors.toSet());
        OWLOntology objectPropertyOntology = man.createOntology();
        objectPropertyAxioms.stream().forEach(objectPropertyOntology::addAxiom);
        man.saveOntology(objectPropertyOntology, turtleFormat, IRI.create(new File("data/objectPropertyOntology.owl")));

        Set<OWLDataPropertyAxiom> dataPropertyAxioms = allAxioms.stream()
                .filter(axiom -> axiom instanceof OWLDataPropertyAxiom)
                .map(axiom -> (OWLDataPropertyAxiom) axiom)
                .collect(Collectors.toSet());
        OWLOntology dataPropertyOntology = man.createOntology();
        dataPropertyAxioms.stream().forEach(dataPropertyOntology::addAxiom);
        man.saveOntology(dataPropertyOntology, turtleFormat, IRI.create(new File("data/dataPropertyOntology.owl")));

        Set<OWLIndividualAxiom> individualAxioms = allAxioms.stream()
                .filter(axiom -> axiom instanceof OWLIndividualAxiom)
                .map(axiom -> (OWLIndividualAxiom) axiom)
                .collect(Collectors.toSet());
        OWLOntology individualOntology = man.createOntology();
        individualAxioms.stream().forEach(individualOntology::addAxiom);
        man.saveOntology(individualOntology, turtleFormat, IRI.create(new File("data/individualOntology.owl")));
    }
}
