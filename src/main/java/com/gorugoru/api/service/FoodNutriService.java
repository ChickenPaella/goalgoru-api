package com.gorugoru.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gorugoru.api.domain.model.FoodNutri;
import com.gorugoru.api.domain.repository.FoodNutriRepository;

@Service
public class FoodNutriService {
	
	@Autowired
	FoodNutriRepository foodNutriRepository;
	
	public FoodNutri insertFoodNutri(FoodNutri foodNutri){
		foodNutri = foodNutriRepository.save(foodNutri);
		return foodNutri;
	}
	
	public List<FoodNutri> getUserList(){
		List<FoodNutri> list = (List<FoodNutri>) foodNutriRepository.findAll();
		return list;
	}
}
