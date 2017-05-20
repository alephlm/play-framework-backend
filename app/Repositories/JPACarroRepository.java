package Repositories;

import models.Carro;
import net.jodah.failsafe.CircuitBreaker;
import net.jodah.failsafe.Failsafe;
import org.joda.time.DateTime;
import play.db.jpa.JPAApi;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.concurrent.CompletableFuture.supplyAsync;

/**
 * Created by aleph on 16/05/17.
 */
@Singleton
public class JPACarroRepository implements CarroRepository {
    private final JPAApi jpaApi;
    private final DatabaseExecutionContext ec;
    private final CircuitBreaker circuitBreaker = new CircuitBreaker().withFailureThreshold(1).withSuccessThreshold(3);

    @Inject
    public JPACarroRepository(JPAApi api, DatabaseExecutionContext ec) {
        this.jpaApi = api;
        this.ec = ec;
    }

    @Override
    public CompletionStage<Stream<Carro>> list() {
        return supplyAsync(() -> wrap(em -> select(em)), ec);
    }

    @Override
    public CompletionStage<Carro> create(Carro carro) {
        return supplyAsync(() -> wrap(em -> insert(em, carro)), ec);
    }

    @Override
    public CompletionStage<Optional<Carro>> update(Long id, Carro carro) {
        return supplyAsync(() -> wrap(em -> Failsafe.with(circuitBreaker).get(() -> modify(em, id, carro))), ec);
    }

    @Override
    public CompletionStage<String> delete(Carro carro) {
        return supplyAsync(() -> wrap(em -> delete(em, carro)), ec);
    }

    private <T> T wrap(Function<EntityManager, T> function) {
        return jpaApi.withTransaction(function);
    }

    private Stream<Carro> list(EntityManager em) {
        List<Carro> carros = em.createQuery("select p from Carro p", Carro.class).getResultList();
        return carros.stream();
    }

    private Stream<Carro> select(EntityManager em) {
        TypedQuery<Carro> query = em.createQuery("SELECT p FROM Carro p", Carro.class);
        return query.getResultList().stream();
    }
    private Optional<Carro> modify(EntityManager em, Long id, Carro carro) throws InterruptedException {
        Carro data = em.find(Carro.class, id);
        data = carro;
        data.id = id;
        em.merge(data);
        return Optional.ofNullable(data);
    }

    private Carro insert(EntityManager em, Carro carro) {
        int ano = new DateTime().getYear() - 30;
        if(carro.ano < ano){
            throw new IllegalArgumentException("Ano deve ser maior que " + ano);
        }
        return em.merge(carro);
    }

    private String delete(EntityManager em, Carro carro) {
        Carro c = em.merge(carro);
        em.remove(c);
        return "OK";
    }

}
