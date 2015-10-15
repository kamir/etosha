/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.computergodzilla.cosinesimilarity;

/**
 *
 * @author kamir
 */
public class DistanceUtility {
 
    /**
     * Manhattan distance
     *
     * @param h1
     * @param h2
     * @return
     */
    public static double distL1(int[] h1, int[] h2) {
        double sum = 0d;
        for (int i = 0; i < h1.length; i++) {
            sum += Math.abs(h1[i] - h2[i]);
        }
        return sum / h1.length;
    }

    public static double distL1(double[] h1, double[] h2) {
        double sum = 0d;
        for (int i = 0; i < h1.length; i++) {
            sum += Math.abs(h1[i] - h2[i]);
        }
        return sum / h1.length;
    }

    /**
     * Euclidean distance
     *
     * @param h1
     * @param h2
     * @return
     */
    public static double distL2(int[] h1, int[] h2) {
        double sum = 0d;
        for (int i = 0; i < h1.length; i++) {
            sum += (h1[i] - h2[i]) * (h1[i] - h2[i]);
        }
        return Math.sqrt(sum);
    }

    /**
     * Euclidean distance
     *
     * @param h1
     * @param h2
     * @return
     */
    public static double distL2(double[] h1, double[] h2) {
        double sum = 0d;
        for (int i = 0; i < h1.length; i++) {
            sum += (h1[i] - h2[i]) * (h1[i] - h2[i]);
        }
        return Math.sqrt(sum);
    }

    /**
     * Euclidean distance
     *
     * @param h1
     * @param h2
     * @return
     */
    public static float distL2(float[] h1, float[] h2) {
        float sum = 0f;
        for (int i = 0; i < h1.length; i++) {
            sum += (h1[i] - h2[i]) * (h1[i] - h2[i]);
        }
        return (float) Math.sqrt(sum);
    }

    /**
     * Jeffrey Divergence or Jensen-Shannon divergence (JSD) from
     * Deselaers, T.; Keysers, D. & Ney, H. Features for image retrieval:
     * an experimental comparison Inf. Retr., Kluwer Academic Publishers, 2008, 11, 77-107
     *
     * @param h1
     * @param h2
     * @return
     */
    public static double jsd(int[] h1, int[] h2) {
        double sum = 0d;
        for (int i = 0; i < h1.length; i++) {
            sum += (h1[i] > 0 ? h1[i] * Math.log(2d * h1[i] / (h1[i] + h2[i])) : 0) +
                    (h2[i] > 0 ? h2[i] * Math.log(2d * h2[i] / (h1[i] + h2[i])) : 0);
        }
        return sum;
    }

    public static float jsd(float[] h1, float[] h2) {
        float sum = 0f;
        for (int i = 0; i < h1.length; i++) {
            sum += (h1[i] > 0 ? (h1[i] / 2f) * Math.log((2f * h1[i]) / (h1[i] + h2[i])) : 0) +
                    (h2[i] > 0 ? (h2[i] / 2f) * Math.log((2f * h2[i]) / (h1[i] + h2[i])) : 0);
        }
        return sum;
    }

    public static float jsd(double[] h1, double[] h2) {
        double sum = 0f;
        for (int i = 0; i < h1.length; i++) {
            sum += (h1[i] > 0 ? (h1[i] / 2f) * Math.log((2f * h1[i]) / (h1[i] + h2[i])) : 0) +
                    (h2[i] > 0 ? (h2[i] / 2f) * Math.log((2f * h2[i]) / (h1[i] + h2[i])) : 0);
        }
        return (float) sum;
    }

    public static double tanimoto(int[] h1, int[] h2) {
        double result = 0;
        double tmp1 = 0;
        double tmp2 = 0;

        double tmpCnt1 = 0, tmpCnt2 = 0, tmpCnt3 = 0;

        for (int i = 0; i < h1.length; i++) {
            tmp1 += h1[i];
            tmp2 += h2[i];
        }

        if (tmp1 == 0 && tmp2 == 0) return 0;
        if (tmp1 == 0 || tmp2 == 0) return 100;

        if (tmp1 > 0 && tmp2 > 0) {
            for (int i = 0; i < h1.length; i++) {
                tmpCnt1 += (h1[i] / tmp1) * (h2[i] / tmp2);
                tmpCnt2 += (h2[i] / tmp2) * (h2[i] / tmp2);
                tmpCnt3 += (h1[i] / tmp1) * (h1[i] / tmp1);

            }

            result = (100 - 100 * (tmpCnt1 / (tmpCnt2 + tmpCnt3
                    - tmpCnt1))); //Tanimoto
        }
        return result;
    }

    public static double tanimoto(float[] h1, float[] h2) {
        double result = 0;
        double tmp1 = 0;
        double tmp2 = 0;

        double tmpCnt1 = 0, tmpCnt2 = 0, tmpCnt3 = 0;

        for (int i = 0; i < h1.length; i++) {
            tmp1 += h1[i];
            tmp2 += h2[i];
        }

        if (tmp1 == 0 && tmp2 == 0) return 0;
        if (tmp1 == 0 || tmp2 == 0) return 100;

        if (tmp1 > 0 && tmp2 > 0) {
            for (int i = 0; i < h1.length; i++) {
                tmpCnt1 += (h1[i] / tmp1) * (h2[i] / tmp2);
                tmpCnt2 += (h2[i] / tmp2) * (h2[i] / tmp2);
                tmpCnt3 += (h1[i] / tmp1) * (h1[i] / tmp1);

            }

            result = (100 - 100 * (tmpCnt1 / (tmpCnt2 + tmpCnt3
                    - tmpCnt1))); //Tanimoto
        }
        return result;
    }

    public static double tanimoto(double[] h1, double[] h2) {
        double result = 0;
        double tmp1 = 0;
        double tmp2 = 0;

        double tmpCnt1 = 0, tmpCnt2 = 0, tmpCnt3 = 0;

        for (int i = 0; i < h1.length; i++) {
            tmp1 += h1[i];
            tmp2 += h2[i];
        }

        if (tmp1 == 0 && tmp2 == 0) return 0;
        if (tmp1 == 0 || tmp2 == 0) return 100;

        if (tmp1 > 0 && tmp2 > 0) {
            for (int i = 0; i < h1.length; i++) {
                tmpCnt1 += (h1[i] / tmp1) * (h2[i] / tmp2);
                tmpCnt2 += (h2[i] / tmp2) * (h2[i] / tmp2);
                tmpCnt3 += (h1[i] / tmp1) * (h1[i] / tmp1);

            }

            result = (100 - 100 * (tmpCnt1 / (tmpCnt2 + tmpCnt3 - tmpCnt1))); //Tanimoto
        }
        return result;
    }

    public static double cosineCoefficient(double[] hist1, double[] hist2) {
        double distance = 0;
        double tmp1 = 0, tmp2 = 0;
        for (int i = 0; i < hist1.length; i++) {
            distance += hist1[i] * hist2[i];
            tmp1 += hist1[i] * hist1[i];
            tmp2 += hist2[i] * hist2[i];
        }
        if (tmp1 * tmp2 > 0) {
            return (distance / (Math.sqrt(tmp1) * Math.sqrt(tmp2)));
        } else return 0d;
    }

    public static float distL1(float[] h1, float[] h2) {
        float sum = 0f;
        for (int i = 0; i < h1.length; i++) {
            sum += Math.abs(h1[i] - h2[i]);
        }
        return sum;
    }

    public static double distL1(byte[] h1, byte[] h2) {
        double sum = 0f;
        for (int i = 0; i < h1.length; i++) {
            sum += Math.abs(h1[i] - h2[i]);
        }
        return sum;
    }
}