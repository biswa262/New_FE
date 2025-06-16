package com.example.e_comerce.service;

import com.example.e_comerce.exception.ProductException;
import com.example.e_comerce.model.Category;
import com.example.e_comerce.model.Product;
import com.example.e_comerce.repository.CategoryRepository;
import com.example.e_comerce.repository.ProductRepository;
import com.example.e_comerce.request.CreateProductRequest;
import com.example.e_comerce.response.ProductResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class ProductServiceImplementation implements  ProductService{

    private ProductRepository productRepository;

    private UserService userService;

    private CategoryRepository categoryRepository;

    public ProductServiceImplementation(ProductRepository productRepository,UserService userService,  CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.userService = userService;
        this.categoryRepository = categoryRepository;
    }
///////////////////
    public ProductResponse createProduct(CreateProductRequest req) {
    	 
    	// Find or create top-level category       
    	    	List<Category> toplevelList=categoryRepository.findByNameIgnoreCase(req.getTopLevelCategory());
    	    	Category topLevel=toplevelList.isEmpty()? null: toplevelList.get(0);
    	    	
    	        if (topLevel == null) {
    	            topLevel = new Category();
    	            topLevel.setName(req.getTopLevelCategory());
    	            topLevel.setLevel(1);
    	            topLevel = categoryRepository.save(topLevel);
    	        }
    	 
    	// Find or create second-level category under topLevel
    	        Category secondLevel = categoryRepository.findByNameAndParent(req.getSecondLevelCategory(), topLevel);
    	        if (secondLevel == null) {
    	            secondLevel = new Category();
    	            secondLevel.setName(req.getSecondLevelCategory());
    	            secondLevel.setParentCategory(topLevel); // associate parent
    	            secondLevel.setLevel(2);
    	            secondLevel = categoryRepository.save(secondLevel);
    	        }
    	 
    	// Find or create third-level category under secondLevel
    	        Category thirdLevel = categoryRepository.findByNameAndParent(req.getThirdLevelCategory(), secondLevel);
    	        if (thirdLevel == null) {
    	            thirdLevel = new Category();
    	            thirdLevel.setName(req.getThirdLevelCategory());
    	            thirdLevel.setParentCategory(secondLevel); // associate parent
    	            thirdLevel.setLevel(3);
    	            thirdLevel = categoryRepository.save(thirdLevel);
    	        }
    	 
    	 
    	        // Create product entity and set properties from request
    	        Product product = new Product();
    	        product.setTitle(req.getTitle());
    	        product.setDescription(req.getDescription());
    	        product.setPrice(req.getPrice());
    	        product.setDiscountedPrice(req.getDicountedPrice());   // note the field name typo here
    	        product.setDiscountedPercent(req.getDicountPercent()); // same typo here
    	        product.setBrand(req.getBrand());
    	        product.setColor(req.getColor());
    	        product.setSizes(req.getSize());
    	        product.setImageUrl(req.getImageUrl());
    	        product.setQuantity(req.getQuantity());
    	        product.setCategory(thirdLevel);  // associate product with third-level category
    	        product.setCreatedAt(LocalDateTime.now());
    	 
    	        // Save the product
    	        Product savedProduct = productRepository.save(product);
    	 
    	        // Map entity to response DTO and return
    	        return mapToProductResponse(savedProduct);
    	    }
    	 
    	 
    	    private ProductResponse mapToProductResponse(Product product) {
    	        ProductResponse response = new ProductResponse();
    	        response.setId(product.getId());
    	        response.setTitle(product.getTitle());
    	        response.setDescription(product.getDescription());
    	        response.setPrice(product.getPrice());
    	        response.setDiscountedPrice(product.getDiscountedPrice());
    	        response.setDiscountedPercent(product.getDiscountedPercent());
    	        response.setBrand(product.getBrand());
    	        response.setColor(product.getColor());
    	        response.setSizes(product.getSizes());
    	        response.setImageUrl(product.getImageUrl());
    	        response.setQuantity(product.getQuantity());
    	        response.setCreatedAt(product.getCreatedAt());
    	 
    	        // Extract category levels
    	        Category third = product.getCategory();
    	        if (third != null) {
    	            response.setThirdLevelCategory(third.getName());
    	            Category second = third.getParentCategory();
    	            if (second != null) {
    	                response.setSecondLevelCategory(second.getName());
    	                Category first = second.getParentCategory();
    	                if (first != null) {
    	                    response.setTopLevelCategory(first.getName());
    	                }
    	            }
    	        }
    	 
    	        return response;
    	    }
    	 


    @Override
    public String deleteProduct(Long ProductId) throws ProductException {
        Product product=findProductById(ProductId);
        product.getSizes().clear();
        productRepository.delete(product);
        return "Product Deleted Sucessfully";
    }


    @Override
    public Product updateProduct(Long productId, Product req) throws ProductException {
    	Product existingProduct = productRepository.findById(productId)
    			.orElseThrow(() -> new ProductException("Product with ID " + productId + " not found"));
 
 
    			if (req.getTitle() != null) {
    			existingProduct.setTitle(req.getTitle());
    			}
    			if (req.getDescription() != null) {
    			 existingProduct.setDescription(req.getDescription());
    			}
    			 if (req.getPrice() != 0) {
    			 existingProduct.setPrice(req.getPrice());
    			}
    			 if (req.getImageUrl() != null) {
    			 existingProduct.setImageUrl(req.getImageUrl());
    			}
    			 if (req.getDiscountedPrice() != 0) {
    			 existingProduct.setDiscountedPrice(req.getDiscountedPrice());
    			}
    			 if (req.getQuantity() != 0) {
    			 existingProduct.setQuantity(req.getQuantity());
    			}
    			if (req.getSizes() != null) {
    			 existingProduct.setSizes(req.getSizes());
    			}
    			
 
    			 Product savedProduct = productRepository.save(existingProduct);
    		     return savedProduct;
    }

    @Override
    public Product findProductById(Long id) throws ProductException {
        Optional<Product> opt= productRepository.findById(id);

        if(opt.isPresent()){
            return opt.get();
        }
        throw  new ProductException("Product Not Found With Id"+id);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> recentlyAddedProduct() {
        return productRepository.findTop10ByOrderByCreatedAtDesc();
    }

    ///////////////////////////////////////////
    @Override
    public List<Product> searchProduct(String query) {
        query = query.toLowerCase();
 
        // 1. Text search: title, description, brand, direct category name, etc.
        List<Product> textMatchedProducts = productRepository.searchProduct(query);
 
        // 2. Match by category names (could be multiple with same name)
        List<Category> categories = categoryRepository.findByNameIgnoreCase(query);
        Set<Product> categoryMatchedProducts = new HashSet<>();
 
        if (!categories.isEmpty()) {
            List<Category> allCategories = categoryRepository.findAll();
 
            for (Category category : categories) {
                // collect IDs of matching category and all descendants
                List<Long> categoryIds = allCategories.stream()
                        .filter(c -> isDescendantOf(c, category) || c.getId().equals(category.getId()))
                        .map(Category::getId)
                        .collect(Collectors.toList());
 
                if (!categoryIds.isEmpty()) {
                    categoryMatchedProducts.addAll(productRepository.findByCategoryIds(categoryIds));
                }
            }
        }
 
        // Combine both sets, remove duplicates
        Set<Product> finalResults = new HashSet<>(textMatchedProducts);
        finalResults.addAll(categoryMatchedProducts);
 
        return new ArrayList<>(finalResults);
    }
 

    private boolean isDescendantOf(Category child, Category parent) {
        while (child.getParentCategory() != null) {
            if (child.getParentCategory().getId().equals(parent.getId())) {
                return true;
            }
            child = child.getParentCategory();
        }
        return false;
    }



    @Override
    public List<Product> findProductByCategory(String category) {
        return productRepository.findByCategory(category.toLowerCase());
    }

    @Override
    public Page<Product> getAllProduct(String category, List<String> colors, List<String> sizes, Integer minPrice, Integer maxPrice, Integer minDiscount, String sort, String stock, Integer pageNumber, Integer pageSize) {
        Pageable pageable= PageRequest.of(pageNumber,pageSize);

        List<Product> products=productRepository.filterProducts(category,minPrice,maxPrice, Integer.valueOf(String.valueOf(minDiscount)),sort);
        if (!colors.isEmpty()){
            products=products.stream().filter(p-> colors.stream().anyMatch(c->c.equalsIgnoreCase(p.getColor())))
                    .collect(Collectors.toList());
        } else if
        (stock!=null){
            if (stock.equals("in_stock")){
                products=products.stream().filter(p->p.getQuantity()<1).collect(Collectors.toList());
            }
        }

        int startIndex=(int) pageable.getOffset();
        int endIndex= Math.min(startIndex + pageable.getPageSize(),products.size());

        List<Product> pageContext=products.subList(startIndex, endIndex);
        Page<Product> filteredProducts =new PageImpl<>(pageContext,pageable,products.size());
        return filteredProducts;
    }
}
