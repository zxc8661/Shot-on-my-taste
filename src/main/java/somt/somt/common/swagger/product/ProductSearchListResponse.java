package somt.somt.common.swagger.product;

import io.swagger.v3.oas.annotations.media.Schema;
import somt.somt.common.CustomResponse.CustomResponse;

@Schema(description = "상품 리스트 데이터")
public class ProductSearchListResponse extends CustomResponse<ProductSearchPageResponse> {};
