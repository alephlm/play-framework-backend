package controllers;

import Repositories.CarroRepository;
import com.fasterxml.jackson.databind.JsonNode;
import models.Carro;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

/**
 * Created by aleph on 16/05/17.
 */
public class CarroController extends Controller {

    private HttpExecutionContext ec;
    private CarroRepository repository;

    @Inject
    public CarroController(HttpExecutionContext ec, CarroRepository repository) {
        this.ec = ec;
        this.repository = repository;
    }

    public CompletionStage<Result> create() {
        JsonNode json = request().body().asJson();
        final Carro resource = Json.fromJson(json, Carro.class);
        return repository.create(resource).thenApplyAsync(savedResource -> {
            return created(Json.toJson(savedResource));
        }, ec.current());
    }

    public CompletionStage<Result> list() {
        return repository.list().thenApplyAsync(posts -> {
            final List<Carro> postList = posts.collect(Collectors.toList());
            return ok(Json.toJson(postList));
        }, ec.current());
    }

    public CompletionStage<Result> update(String id) {
        JsonNode json = request().body().asJson();
        final Carro resource = Json.fromJson(json, Carro.class);
        return repository.update(Long.parseLong(id), resource).thenApplyAsync(optionalResource -> {
            return optionalResource.map(r ->
                    ok(Json.toJson(r))
            ).orElseGet(() ->
                    notFound()
            );
        }, ec.current());
    }

    public CompletionStage<Result> delete() {
        JsonNode json = request().body().asJson();
        final Carro resource = Json.fromJson(json, Carro.class);
        return repository.delete(resource).thenApplyAsync(savedResource -> {
            return ok();
        }, ec.current());
    }
}
