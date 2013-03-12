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
    private int denominator;
    @NotNull
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

    public int getDivisor() {
        return divisor;
    }

    public double getPercentage()
    {
        return (((double)this.denominator)/this.divisor)*100;
    }
}
