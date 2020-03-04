package com.kennetheu.gini;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.stream.DoubleStream;

public class CreateData {

    private int[] incomes;
    private double[] cumulativeIncomes;
    private double[] line45;
    private double giniCoef;
    private int sumOfIndividuals;

    public int getSumOfIndividuals() {
        return sumOfIndividuals;
    }

    public int[] getIncomes() {
        return incomes;
    }

    public double[] getCumulativeIncomes() {
        return cumulativeIncomes;
    }

    public double[] getLine45() {
        return line45;
    }

    public double getGiniCoef() {
        return giniCoef;
    }

    public CreateData(int groups, int groupSpan, int[] groupSizes) {
        // This constructor passes the parameters to the CreateIncomeDistribution
        this.incomes = new CreateIncomeDistribution(groups, groupSpan, groupSizes).getIncomeDistribution();
        this.cumulativeIncomes = getCumulativeIncomes(incomes);
        this.line45 = makeLine45();
        this.giniCoef = calcGiniCoef();
        this.sumOfIndividuals = new CreateIncomeDistribution(groups, groupSpan, groupSizes).getSumOfIndividuals();
    }

    public CreateData(CreateIncomeDistribution createIncomeDistribution) {
        // This constructor takes a CreateIncomeDistribution object as the parameter
        this.incomes = createIncomeDistribution.getIncomeDistribution();
        this.cumulativeIncomes = getCumulativeIncomes(incomes);
        this.line45 = makeLine45();
        this.giniCoef = calcGiniCoef();
        this.sumOfIndividuals = createIncomeDistribution.getSumOfIndividuals();
    }

    public CreateData(int[] incomes) {
        /* This constructor takes an user made income array
        and performs the calculations */
        this.incomes = incomes;
        this.cumulativeIncomes = getCumulativeIncomes(incomes);
        this.line45 = makeLine45();
        this.giniCoef = calcGiniCoef();
        this.sumOfIndividuals = incomes.length;
    }

    public CreateData(int i, int minIncome, int maxIncome) {
        this.incomes = getIncomeArray(i, minIncome, maxIncome);
        this.cumulativeIncomes = getCumulativeIncomes(incomes);
        this.line45 = makeLine45();
        this.giniCoef = calcGiniCoef();
        this.sumOfIndividuals = i;
    }

    public int[] getIncomeArray(int incomes, int minIncome, int maxIncome) {
        int[] incomeArray = new int[incomes];
        for (int i = 0; i < incomeArray.length; i++) {
            incomeArray[i] = makeIncome(minIncome,maxIncome);
        }
        // Sort the incomes
        Arrays.sort(incomeArray);
        return incomeArray;
    }

    public int makeIncome(int min, int max) {
        return (int)(Math.random() * (max - min + 1) + min);
    }

    public double[] getCumulativeIncomes(int[] incomes) {
        double[] cumulativeArray = new double[incomes.length];
        double prevSum = 0;
        double sum = 0;
        for (int income : incomes) {
            sum += (double)income;
        }
        for ( int i = 0 ; i < incomes.length ; i++ )
        {
            cumulativeArray[i] = (prevSum + incomes[i]) / sum;
            prevSum += incomes[i];
        }
        return cumulativeArray;
    }

    public double[] makeLine45() {
        double[] line45 = new double[incomes.length];
        for (int i = 0; i < incomes.length; i++) {
            line45[i] = (i+1) / (double)incomes.length;
        }
        return line45;
    }

    public double calcGiniCoef() {
        double[] a = new double[incomes.length];

        for (int i = 0; i < incomes.length; i++) {
            a[i] = line45[i] - cumulativeIncomes[i];
        }
        return DoubleStream.of(a).sum() / (DoubleStream.of(cumulativeIncomes).sum() + DoubleStream.of(a).sum());
    }

    public void printIncomes() {
        DecimalFormat df=new DecimalFormat("0.000");
        int printEvery = sumOfIndividuals / 9;
        int i = 0;
        System.out.println("----Printing starting----");
        while (i < this.incomes.length) {
            System.out.println(i+1 + "\tInc: " + this.incomes[i] + "\tCum: " + df.format(this.cumulativeIncomes[i]) + "\tLine45: " + df.format(this.line45[i]));
            i = i+printEvery;
        }
        if (i-printEvery != sumOfIndividuals-1) {
            System.out.println(sumOfIndividuals + "\tInc: " + this.incomes[sumOfIndividuals-1] + "\tCum: " + df.format(this.cumulativeIncomes[sumOfIndividuals-1]) + "\tLine45: " + df.format(this.line45[sumOfIndividuals-1]));
        }
        System.out.println("There is " + sumOfIndividuals + " individuals in the data and the ginicoef is " + df.format(giniCoef));
        System.out.println("-----Printing ended-----");
    }
}
