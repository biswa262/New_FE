package com.example.e_comerce.repository;
 
import com.example.e_comerce.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
 
import java.util.List;
 
///////////////////////////////
public interface CategoryRepository extends JpaRepository<Category,Long> {
    List<Category> findByNameIgnoreCase(String name);
 
    @Query("SELECT c FROM Category c WHERE c.name = :name AND c.parentCategory = :parent")
    Category findByNameAndParent(@Param("name") String name, @Param("parent") Category parent);
 
 
}