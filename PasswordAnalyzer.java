public class PasswordAnalyzer {
    // This class contains all the logic to check password strength
    
    // Method to check if password meets minimum length requirement
    public boolean checkMinimumLength(String password, int minLength) {
        return password.length() >= minLength;
    }
    
    // Method to check if password contains uppercase letters
    public boolean hasUppercase(String password) {
        for (int i = 0; i < password.length(); i++) {
            if (Character.isUpperCase(password.charAt(i))) {
                return true;
            }
        }
        return false;
    }
    
    // Method to check if password contains lowercase letters
    public boolean hasLowercase(String password) {
        for (int i = 0; i < password.length(); i++) {
            if (Character.isLowerCase(password.charAt(i))) {
                return true;
            }
        }
        return false;
    }
    
    // Method to check if password contains numbers
    public boolean hasNumbers(String password) {
        for (int i = 0; i < password.length(); i++) {
            if (Character.isDigit(password.charAt(i))) {
                return true;
            }
        }
        return false;
    }
    
    // Method to check if password contains special characters
    public boolean hasSpecialCharacters(String password) {
        String specialChars = "!@#$%^&*()_+-=[]{}|;:,.<>?";
        for (int i = 0; i < password.length(); i++) {
            if (specialChars.contains(String.valueOf(password.charAt(i)))) {
                return true;
            }
        }
        return false;
    }
    
    // Method to check for common weak passwords
    public boolean isCommonPassword(String password) {
        String[] commonPasswords = {
            "password", "123456", "password123", "admin", "qwerty",
            "letmein", "welcome", "monkey", "dragon", "master"
        };
        
        String lowerPassword = password.toLowerCase();
        for (String common : commonPasswords) {
            if (lowerPassword.equals(common)) {
                return true;
            }
        }
        return false;
    }
    
    // Method to calculate overall password strength score
    public int calculateStrengthScore(String password) {
        int score = 0;
        
        // Add points for each requirement met
        if (checkMinimumLength(password, 8)) score += 20;
        if (hasUppercase(password)) score += 20;
        if (hasLowercase(password)) score += 20;
        if (hasNumbers(password)) score += 20;
        if (hasSpecialCharacters(password)) score += 20;
        
        // Subtract points for common passwords
        if (isCommonPassword(password)) score -= 50;
        
        // Bonus points for longer passwords
        if (password.length() >= 12) score += 10;
        if (password.length() >= 16) score += 10;
        
        return Math.max(0, score); // Ensure score is never negative
    }
    
    // Method to get strength level description
    public String getStrengthLevel(int score) {
        if (score >= 80) return "STRONG";
        else if (score >= 60) return "MODERATE";
        else if (score >= 40) return "WEAK";
        else return "VERY WEAK";
    }
}
