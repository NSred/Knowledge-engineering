package com.owl.api.example.service;

import com.owl.api.example.dto.CPURequestDTO;
import com.owl.api.example.dto.CPUResponseDTO;
import org.semanticweb.HermiT.ReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.vocab.OWLFacet;
import org.semanticweb.owlapi.vocab.XSDVocabulary;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@Service
public class CPUService {

    private OntologyManager ontologyManager;

    public CPUService() throws OWLOntologyCreationException, OWLOntologyStorageException {
        this.ontologyManager = new OntologyManager();
    }

    public List<CPUResponseDTO> getCPUBySpec(CPURequestDTO cpu) {
        OWLOntologyManager man = OWLManager.createOWLOntologyManager();

        OWLDataFactory df = man.getOWLDataFactory();
        OWLReasonerFactory rf = new ReasonerFactory();
        OWLReasoner r = rf.createReasoner(this.ontologyManager.getOntology());

        OWLClassExpression classCPU = df.getOWLClass(IRI.create("http://www.semanticweb.org/administrator/ontologies/2023/2/untitled-ontology-3#CPU"));

        OWLDataProperty hasCoresProperty = df.getOWLDataProperty(IRI.create("http://www.semanticweb.org/administrator/ontologies/2023/2/untitled-ontology-3#cpu_has_cores"));
        OWLDataRange coresRange = df.getOWLDatatypeRestriction(df.getIntegerOWLDatatype(),
                df.getOWLFacetRestriction(OWLFacet.MIN_INCLUSIVE, df.getOWLLiteral(cpu.getLowerCores())),
                df.getOWLFacetRestriction(OWLFacet.MAX_INCLUSIVE, df.getOWLLiteral(cpu.getHigherCores())));

        OWLDataProperty hasThreadsProperty = df.getOWLDataProperty(IRI.create("http://www.semanticweb.org/administrator/ontologies/2023/2/untitled-ontology-3#cpu_has_threads"));
        OWLDataRange threadsRange = df.getOWLDatatypeRestriction(df.getIntegerOWLDatatype(),
                df.getOWLFacetRestriction(OWLFacet.MIN_INCLUSIVE, df.getOWLLiteral(cpu.getLowerThreads())),
                df.getOWLFacetRestriction(OWLFacet.MAX_INCLUSIVE, df.getOWLLiteral(cpu.getHigherThreads())));

        OWLDataProperty hasClockSpeedProperty = df.getOWLDataProperty(IRI.create("http://www.semanticweb.org/administrator/ontologies/2023/2/untitled-ontology-3#cpu_has_clock_speed_in_ghz"));
        OWLDataRange clockSpeedRange = df.getOWLDatatypeRestriction(df.getOWLDatatype(XSDVocabulary.DECIMAL.getIRI()),
                df.getOWLFacetRestriction(OWLFacet.MIN_INCLUSIVE, df.getOWLLiteral(String.valueOf(cpu.getLowerClockSpeed()), df.getOWLDatatype(XSDVocabulary.DECIMAL.getIRI()))),
                df.getOWLFacetRestriction(OWLFacet.MAX_INCLUSIVE, df.getOWLLiteral(String.valueOf(cpu.getHigherClockSpeed()), df.getOWLDatatype(XSDVocabulary.DECIMAL.getIRI()))));

        OWLDataProperty hasTDPProperty = df.getOWLDataProperty(IRI.create("http://www.semanticweb.org/administrator/ontologies/2023/2/untitled-ontology-3#cpu_has_tdp_in_watts"));
        OWLDataRange TDPRange = df.getOWLDatatypeRestriction(df.getIntegerOWLDatatype(),
                df.getOWLFacetRestriction(OWLFacet.MIN_INCLUSIVE, df.getOWLLiteral(cpu.getLowerTDP())),
                df.getOWLFacetRestriction(OWLFacet.MAX_INCLUSIVE, df.getOWLLiteral(cpu.getHigherTDP())));

        OWLClassExpression queryExpression = df.getOWLObjectIntersectionOf(
                classCPU,
                df.getOWLDataSomeValuesFrom(hasCoresProperty, coresRange),
                df.getOWLDataSomeValuesFrom(hasThreadsProperty, threadsRange),
                df.getOWLDataSomeValuesFrom(hasTDPProperty, TDPRange),
                df.getOWLDataSomeValuesFrom(hasClockSpeedProperty, clockSpeedRange)
                );
        Set<OWLNamedIndividual> individuals = r.getInstances(queryExpression, false).getFlattened();
        List<CPUResponseDTO> cpus = new ArrayList<>();
        for (OWLNamedIndividual individual : individuals) {
            cpus.add(setSpec(df, individual));
        }
        return cpus;
    }

    private CPUResponseDTO setSpec(OWLDataFactory df, OWLNamedIndividual individual) {
        CPUResponseDTO cpuResponseDTO = new CPUResponseDTO();
        OWLDataProperty dp = df.getOWLDataProperty(IRI.create("http://www.semanticweb.org/administrator/ontologies/2023/2/untitled-ontology-3#cpu_has_name"));
        for (OWLDataPropertyAssertionAxiom assertion : this.ontologyManager.getOntology().getDataPropertyAssertionAxioms(individual)) {
            OWLDataProperty property = assertion.getProperty().asOWLDataProperty();
            if (dp.equals(property)) {
                OWLLiteral value = assertion.getObject();
                cpuResponseDTO.setName(value.getLiteral());
            }
        }
        dp = df.getOWLDataProperty(IRI.create("http://www.semanticweb.org/administrator/ontologies/2023/2/untitled-ontology-3#cpu_has_tdp_in_watts"));
        for (OWLDataPropertyAssertionAxiom assertion : this.ontologyManager.getOntology().getDataPropertyAssertionAxioms(individual)) {
            OWLDataProperty property = assertion.getProperty().asOWLDataProperty();
            if (dp.equals(property)) {
                OWLLiteral value = assertion.getObject();
                cpuResponseDTO.setTDP(Integer.parseInt(value.getLiteral()));
            }
        }
        dp = df.getOWLDataProperty(IRI.create("http://www.semanticweb.org/administrator/ontologies/2023/2/untitled-ontology-3#cpu_has_cores"));
        for (OWLDataPropertyAssertionAxiom assertion : this.ontologyManager.getOntology().getDataPropertyAssertionAxioms(individual)) {
            OWLDataProperty property = assertion.getProperty().asOWLDataProperty();
            if (dp.equals(property)) {
                OWLLiteral value = assertion.getObject();
                cpuResponseDTO.setCores(Integer.parseInt(value.getLiteral()));
            }
        }
        dp = df.getOWLDataProperty(IRI.create("http://www.semanticweb.org/administrator/ontologies/2023/2/untitled-ontology-3#cpu_has_threads"));
        for (OWLDataPropertyAssertionAxiom assertion : this.ontologyManager.getOntology().getDataPropertyAssertionAxioms(individual)) {
            OWLDataProperty property = assertion.getProperty().asOWLDataProperty();
            if (dp.equals(property)) {
                OWLLiteral value = assertion.getObject();
                cpuResponseDTO.setThreads(Integer.parseInt(value.getLiteral()));
            }
        }
        dp = df.getOWLDataProperty(IRI.create("http://www.semanticweb.org/administrator/ontologies/2023/2/untitled-ontology-3#cpu_has_clock_speed_in_ghz"));
        for (OWLDataPropertyAssertionAxiom assertion : this.ontologyManager.getOntology().getDataPropertyAssertionAxioms(individual)) {
            OWLDataProperty property = assertion.getProperty().asOWLDataProperty();
            if (dp.equals(property)) {
                OWLLiteral value = assertion.getObject();
                cpuResponseDTO.setClockSpeed(Double.parseDouble(value.getLiteral()));
            }
        }
        return cpuResponseDTO;
    }
}
