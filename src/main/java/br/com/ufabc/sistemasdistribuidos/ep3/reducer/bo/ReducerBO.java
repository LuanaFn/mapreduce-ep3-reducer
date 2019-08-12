package br.com.ufabc.sistemasdistribuidos.ep3.reducer.bo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ReducerBO {
	List<String> entradas;
	int nmappers;

	public ReducerBO(int nmappers) {
		this.nmappers = nmappers;
	}

	public void recebeLinks(String links) {
		if (entradas == null) {
			entradas = new ArrayList<String>();
		}

		entradas.add(links);

		if (entradas.size() >= nmappers) {
			ObjectMapper mapper = new ObjectMapper();
			HashMap<String, List<String>> map = new HashMap<String, List<String>>();
			
			for(int i = 0; i < entradas.size(); i++) {
				try {
					map.putAll(mapper.readValue(entradas.get(i), map.getClass()));
				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			reduce(map);
		}
	}

	private void reduce(HashMap<String, List<String>> map) {
		System.out.println("Iniciando processamento do map");
	}
}
