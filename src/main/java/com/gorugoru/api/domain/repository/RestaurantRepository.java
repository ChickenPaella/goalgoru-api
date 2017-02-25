package com.gorugoru.api.domain.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gorugoru.api.domain.model.Restaurant;

@Transactional
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
	
	public abstract List<Restaurant> findByDongAndCategoryOrderByNameAsc(String dong, String cate);
	public abstract List<Restaurant> findByDongAndCategoryOrderByNameDesc(String dong, String cate);
	public abstract List<Restaurant> findByCategoryOrderByNameAsc(String cate);
	public abstract List<Restaurant> findByCategoryOrderByNameDesc(String cate);
	
	@Query("select t.restaurant from RestaurantLocation t where (6371*acos(cos(radians(:latitude))*cos(radians(t.latitude))*cos(radians(t.longitude)-radians(:longitude))+sin(radians(:latitude))*sin(radians(t.latitude)))) < 1 order by (6371*acos(cos(radians(:latitude))*cos(radians(t.latitude))*cos(radians(t.longitude)-radians(:longitude))+sin(radians(:latitude))*sin(radians(t.latitude))))")
	public abstract List<Restaurant> findByCoord(@Param("latitude") double latitude, @Param("longitude") double longitude);
}

