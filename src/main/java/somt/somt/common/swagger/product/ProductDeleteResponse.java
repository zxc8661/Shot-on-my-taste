package somt.somt.common.swagger.product;


import io.swagger.v3.oas.annotations.media.Schema;
import somt.somt.common.CustomResponse.CustomResponse;

@Schema(description = "상품 삭제 성공 응답")
class ProductDeleteResponse extends CustomResponse<String> {};