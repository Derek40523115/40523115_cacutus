package com.productphoto.model;

import java.util.List;


public interface ProductPhotoDAO_interface {
	public void insert(ProductPhotoVo productPhotoVO);
	public void update(ProductPhotoVo productPhotoVO);
	public void delete(Integer productPhotoId);
	public ProductPhotoVo findByPrimaryKey(Integer productPhotoId);
	public List<ProductPhotoVo> getAll();
}
