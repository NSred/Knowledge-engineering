package com.owl.api.example.service;

import org.semanticweb.owlapi.apibinding.OWLManager;
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

        OWLOntology ontology = man.loadOntologyFromOntologyDocument(new File("data/knowledge-base.owl"));
        saveOntologyIntoSeparateFiles(man, ontology);

        OWLOntology individualsOntology = man.loadOntologyFromOntologyDocument(new File("data/individualOntology.owl"));
        OWLOntology classesOntology = man.loadOntologyFromOntologyDocument(new File("data/classOntology.owl"));
        OWLOntology objectPropertiesOntology = man.loadOntologyFromOntologyDocument(new File("data/objectPropertyOntology.owl"));
        OWLOntology dataPropertiesOntology = man.loadOntologyFromOntologyDocument(new File("data/dataPropertyOntology.owl"));

        OWLOntology combinedOntology = man.createOntology();

        man.addAxioms(combinedOntology, individualsOntology.getAxioms());
        man.addAxioms(combinedOntology, classesOntology.getAxioms());
        man.addAxioms(combinedOntology, objectPropertiesOntology.getAxioms());
        man.addAxioms(combinedOntology, dataPropertiesOntology.getAxioms());

        this.ontology = combinedOntology;
    }

    private static void saveOntologyIntoSeparateFiles(OWLOntologyManager man, OWLOntology ontology) throws OWLOntologyCreationException, OWLOntologyStorageException {
        Set<OWLAxiom> allAxioms = ontology.getAxioms();

        Set<OWLClassAxiom> classAxioms = allAxioms.stream()
                .filter(axiom -> axiom instanceof OWLClassAxiom)
                .map(axiom -> (OWLClassAxiom) axiom)
                .collect(Collectors.toSet());
        OWLOntology classOntology = man.createOntology();
        classAxioms.stream().forEach(classOntology::addAxiom);
        man.saveOntology(classOntology, IRI.create(new File("data/classOntology.owl")));

        Set<OWLObjectPropertyAxiom> objectPropertyAxioms = allAxioms.stream()
                .filter(axiom -> axiom instanceof OWLObjectPropertyAxiom)
                .map(axiom -> (OWLObjectPropertyAxiom) axiom)
                .collect(Collectors.toSet());
        OWLOntology objectPropertyOntology = man.createOntology();
        objectPropertyAxioms.stream().forEach(objectPropertyOntology::addAxiom);
        man.saveOntology(objectPropertyOntology, IRI.create(new File("data/objectPropertyOntology.owl")));

        Set<OWLDataPropertyAxiom> dataPropertyAxioms = allAxioms.stream()
                .filter(axiom -> axiom instanceof OWLDataPropertyAxiom)
                .map(axiom -> (OWLDataPropertyAxiom) axiom)
                .collect(Collectors.toSet());
        OWLOntology dataPropertyOntology = man.createOntology();
        dataPropertyAxioms.stream().forEach(dataPropertyOntology::addAxiom);
        man.saveOntology(dataPropertyOntology, IRI.create(new File("data/dataPropertyOntology.owl")));

        Set<OWLIndividualAxiom> individualAxioms = allAxioms.stream()
                .filter(axiom -> axiom instanceof OWLIndividualAxiom)
                .map(axiom -> (OWLIndividualAxiom) axiom)
                .collect(Collectors.toSet());
        OWLOntology individualOntology = man.createOntology();
        individualAxioms.stream().forEach(individualOntology::addAxiom);
        man.saveOntology(individualOntology, IRI.create(new File("data/individualOntology.owl")));
    }
}
