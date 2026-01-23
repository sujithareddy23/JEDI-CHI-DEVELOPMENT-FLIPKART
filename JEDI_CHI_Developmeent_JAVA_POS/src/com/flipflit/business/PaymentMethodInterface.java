/**
 * 
 */
package com.flipflit.business;

/**
 * 
 */
public interface PaymentMethodInterface {
	boolean validateDetails(String details);
    String processTransaction(double amount);
    boolean authorizePayment(String transactionId);

}
