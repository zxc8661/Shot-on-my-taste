package somt.somt.common.swagger.product;

import io.swagger.v3.oas.annotations.media.Schema;
import somt.somt.common.CustomResponse.CustomResponse;
import somt.somt.domain.product.dto.reponse.ProductDetailDTO;

@Schema(description = "상품 상세 응답")
public class ProductDetailResponse extends CustomResponse<ProductDetailDTO> {};