package br.com.ufabc.sistemasdistribuidos.ep3.reducer.bo;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.ufabc.sistemasdistribuidos.ep3.reducer.dto.Dto;
import br.com.ufabc.sistemasdistribuidos.ep3.reducer.tcp.TCPClient;

public class ReducerBO {
	List<Dto> entradas;
	int nmappers;
	int maxRef;
	Dto dto;

	public ReducerBO(int nmappers) {
		this.nmappers = nmappers;
		this.dto = new Dto();
	}

	public void recebeLinks(String links) {

		// traduz o dto que o mapper mandou
		ObjectMapper mapper = new ObjectMapper();
		try {
			dto = mapper.readValue(links, Dto.class);
			
			if (entradas == null) {
				entradas = new ArrayList<Dto>();
			}
			
			entradas.add(dto);

		} catch (JsonProcessingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		if (entradas.size() >= nmappers) {
			
			HashMap<String, List<String>> map = new HashMap<String, List<String>>();

			for (int i = 0; i < entradas.size(); i++) {
				map.putAll(entradas.get(i).getLinksMap());
			}

			reduce(map);
		}
	}

	private void reduce(HashMap<String, List<String>> map) {
		System.out.println("Iniciando processamento do map");

		HashMap<String, List<String>> invertido = new HashMap<String, List<String>>();

		// para cada url key, vai juntando sua estrutura de dados
		map.forEach((key, value) -> {

			// para cada link da lista de links que a key aponta, cria um novo índice no
			// mapa invertido
			value.forEach(linkApontado -> {

				// verifica se o link já é um índice na mapa invertido
				if (!invertido.containsKey(linkApontado))
					invertido.put(linkApontado, new ArrayList<String>());

				// adiciona o site onde o link foi referenciado para sua lista
				invertido.get(linkApontado).add(key);

				System.out.println("O link " + linkApontado + " é referenciado por " + key);
			});

			// se o site key ainda não tiver sido adicionado por não ser referenciado em
			// lugar nenhum,
			// adiciona ele
			if (!invertido.containsKey(key))
				invertido.put(key, new ArrayList<String>());
		});

		ordena(invertido);
	}

	private void ordena(HashMap<String, List<String>> map) {
		System.out.println("\nIniciando ordenação dos links");

		HashMap<Integer, List<String>> quantidadesMap = new HashMap<Integer, List<String>>();
		HashMap<Integer, String> listaOrdenada = new HashMap<Integer, String>();

		// maximo de referencias que um site possui
		maxRef = 0;

		// passa os valores pra lista de quantidades, sendo do tipo (quantidade de
		// referencias := links com esta quantidade)
		map.forEach((key, value) -> {

			if (!quantidadesMap.containsKey(value.size()))
				quantidadesMap.put(value.size(), new ArrayList<String>());

			quantidadesMap.get(value.size()).add(key);

			// atualiza o maximo das referencias
			if (maxRef < value.size())
				maxRef = value.size();
		});

		ObjectMapper mapper = new ObjectMapper();
		System.out.println("\nContagem de acessos finalizada:");
		// envia a lista com pretty printer para ficar visível
		try {
			System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(quantidadesMap));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		int index = 0;
		while (maxRef >= 0) {

			// captura as urls com aquela quantidade de referências
			List<String> urls = quantidadesMap.get(maxRef);

			// se existir alguma url com aquela quantidade de referencias...
			if (urls != null) {
				for (int i = 0; i < urls.size(); i++) {
					listaOrdenada.put(index, urls.get(i));
					index++;
				}
			}
			maxRef--;
		}

		System.out.println("Fim da ordenação.\n");

		enviaMap(listaOrdenada);
	}

	private void enviaMap(HashMap<Integer, String> listaOrdenada) {
		try {
			TCPClient client = new TCPClient(dto.getHost(), dto.getPort());

			ObjectMapper mapper = new ObjectMapper();

			// envia a lista com pretty printer para ficar visível
			client.enviaMensagem(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(listaOrdenada));
			client.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
