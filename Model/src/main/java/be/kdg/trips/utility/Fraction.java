package be.kdg.trips.utility;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

/**
 * Subversion id
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2012-2013
 */
public class Fraction {
    @NotNull
    @DecimalMin(value = "0")
    @DecimalMax(value = "100")
    private int denominator;
    @NotNull
    @DecimalMin(value = "0")
    @DecimalMax(value = "100")
    private int divisor;

    public Fraction() {
        this.denominator = 1;
        this.divisor = 1;
    }

    public Fraction(int denominator, int divisor) {
        this.denominator = denominator;
        this.divisor = divisor;
    }

    public int getDenominator() {
        return denominator;
    }

    public void setDenominator(int denominator) {
        this.denominator = denominator;
    }

    public int getDivisor() {
        return divisor;
    }

    public void setDivisor(int divisor) {
        this.divisor = divisor;
    }

    public double getDecimal()
    {
        return denominator/divisor;
    }
}
