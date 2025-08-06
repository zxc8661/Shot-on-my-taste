package somt.somt.common.swagger.product;

import io.swagger.v3.oas.annotations.media.Schema;
import somt.somt.common.CustomResponse.CustomResponse;

@Schema(description = "상품 ID 응답")
public class ProductIdResponse extends CustomResponse<Long> {};
