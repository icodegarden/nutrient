package io.github.icodegarden.nutrient.mybatis.interceptor;

class NutrientMybatisException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public NutrientMybatisException(String message) {
        super(message);
    }

    public NutrientMybatisException(Throwable throwable) {
        super(throwable);
    }

    public NutrientMybatisException(String message, Throwable throwable) {
        super(message, throwable);
    }
}