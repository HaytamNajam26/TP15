package ma.projet.graph.controllers;

import lombok.AllArgsConstructor;
import ma.projet.graph.entities.Compte;
import ma.projet.graph.entities.CompteRequest;
import ma.projet.graph.entities.Transaction;
import ma.projet.graph.entities.TransactionRequest;
import ma.projet.graph.repositories.CompteRepository;
import ma.projet.graph.repositories.TransactionRepository;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@AllArgsConstructor
public class CompteControllerGraphQL {
    private CompteRepository compteRepository;
    private TransactionRepository transactionRepository;
    
    @QueryMapping
    public List<Compte> allComptes(){
        return compteRepository.findAll();
    }
    
    @QueryMapping
    public Compte compteById(@Argument Long id){
        Compte compte = compteRepository.findById(id).orElse(null);
        if(compte == null) throw new RuntimeException(String.format("Compte %s not found", id));
        else return compte;
    }
    
    @MutationMapping
    public Compte saveCompte(@Argument CompteRequest compteRequest){
        if (compteRequest == null) {
            throw new RuntimeException("CompteRequest cannot be null");
        }
        
        Compte compte = new Compte();
        compte.setSolde(compteRequest.getSolde() != null ? compteRequest.getSolde() : 0.0);
        compte.setType(compteRequest.getType());
        
        // Conversion de la date depuis String vers Date (format: yyyy/MM/dd)
        if (compteRequest.getDateCreation() != null && !compteRequest.getDateCreation().isEmpty()) {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                Date dateCreation = dateFormat.parse(compteRequest.getDateCreation());
                compte.setDateCreation(dateCreation);
            } catch (ParseException e) {
                throw new RuntimeException("Format de date invalide. Utilisez le format yyyy/MM/dd", e);
            }
        }
        
        Compte savedCompte = compteRepository.saveAndFlush(compte);
        
        // Debug: Vérifier que le compte est bien sauvegardé
        System.out.println("Compte sauvegardé - ID: " + savedCompte.getId() + 
                          ", Solde: " + savedCompte.getSolde() + 
                          ", Type: " + savedCompte.getType() + 
                          ", Date: " + savedCompte.getDateCreation());
        
        return savedCompte;
    }
    
    @QueryMapping
    public Map<String, Object> totalSolde() {
        long count = compteRepository.count(); 
        double sum = compteRepository.sumSoldes() != null ? compteRepository.sumSoldes() : 0.0;
        double average = count > 0 ? sum / count : 0;
        
        return Map.of(
            "count", count,
            "sum", sum,
            "average", average
        );
    }
    
    @MutationMapping
    public Transaction addTransaction(@Argument TransactionRequest transactionRequest) {
        Compte compte = compteRepository.findById(transactionRequest.getCompteId())
            .orElseThrow(() -> new RuntimeException("Compte not found"));
        
        Transaction transaction = new Transaction();
        transaction.setMontant(transactionRequest.getMontant());
        transaction.setType(transactionRequest.getType());
        transaction.setCompte(compte);
        
        // Conversion de la date depuis String vers Date (format: yyyy/MM/dd)
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            Date date = dateFormat.parse(transactionRequest.getDate());
            transaction.setDate(date);
        } catch (ParseException e) {
            throw new RuntimeException("Format de date invalide. Utilisez le format yyyy/MM/dd", e);
        }
        
        return transactionRepository.save(transaction);
    }
    
    @QueryMapping
    public List<Transaction> compteTransactions(@Argument Long id) {
        Compte compte = compteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Compte not found"));
        return transactionRepository.findByCompte(compte);
    }
    
    @QueryMapping
    public List<Transaction> allTransactions() {
        return transactionRepository.findAll();
    }
    
    @QueryMapping
    public Map<String, Object> transactionStats() {
        long count = transactionRepository.count();
        double sumDepots = transactionRepository.sumByType(ma.projet.graph.entities.TypeTransaction.DEPOT) != null 
            ? transactionRepository.sumByType(ma.projet.graph.entities.TypeTransaction.DEPOT) : 0.0;
        double sumRetraits = transactionRepository.sumByType(ma.projet.graph.entities.TypeTransaction.RETRAIT) != null 
            ? transactionRepository.sumByType(ma.projet.graph.entities.TypeTransaction.RETRAIT) : 0.0;
        
        return Map.of(
            "count", count,
            "sumDepots", sumDepots,
            "sumRetraits", sumRetraits
        );
    }
}
