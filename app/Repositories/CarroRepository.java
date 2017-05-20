package Repositories;

import com.google.inject.ImplementedBy;
import models.Carro;

import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;

/**
 * Created by aleph on 16/05/17.
 */
@ImplementedBy(JPACarroRepository.class)
public interface CarroRepository {

    CompletionStage<Stream<Carro>> list();

    CompletionStage<Carro> create(Carro carro);

    CompletionStage<Optional<Carro>> update(Long id, Carro carro);

    CompletionStage<String> delete(Carro carro);

}
