package somt.somt.common.swagger.product;

import io.swagger.v3.oas.annotations.media.Schema;
import somt.somt.common.CustomResponse.CustomPageResponse;
import somt.somt.domain.product.dto.reponse.ProductDTO;

@Schema(description = "상품 검색 페이징 응답 데이터")
class ProductSearchPageResponse extends CustomPageResponse<ProductDTO> {};