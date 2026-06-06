    package com.upt.UniMarket.Entity;

    import jakarta.persistence.*;
    import java.util.Objects;

    @Entity
    @Table(name = "users")
    @Inheritance(strategy = InheritanceType.JOINED)
    @DiscriminatorColumn(name = "role", discriminatorType = DiscriminatorType.STRING)
    public abstract class User {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        @Column(name = "email", nullable = false ,unique = true)
        private String email;
        @Column(name = "password_hash", nullable = false)
        private String passwordHash;
        @Column(name = "role", nullable = false, insertable = false, updatable = false)
        private String role;

        public User(String email, String passwordHash, String role){
            this.email = email;
            this.passwordHash = passwordHash;
            this.role = role;
        }

        public User() {}

        public Long getId() {
            return id;
        }

        public String getEmail() {
            return email;
        }
        public String getPasswordHash() {
            return passwordHash;
        }
        public String getRole() {
            return role;
        }
        public void setRole(String role) {
            this.role = role;
        }
        public void setEmail(String email) {
            this.email = email;
        }
        public void setPasswordHash(String passwordHash) {
            this.passwordHash = passwordHash;
        }


        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (!(obj instanceof User)) {
                return false;
            }
            User user = (User) obj;
            return Objects.equals(id, user.id);
        }
        @Override
        public int hashCode(){
            return Objects.hashCode(id);
        }



    }



