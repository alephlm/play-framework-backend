package models;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "carros")
public class Carro {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Long id;
	@NotNull
	public Long idPessoa;
	@NotNull
	public String modelo;
	@NotNull
	public int ano;
	@NotNull
	@Enumerated(EnumType.STRING)
	public EnumCorDoCarro cor;
}
