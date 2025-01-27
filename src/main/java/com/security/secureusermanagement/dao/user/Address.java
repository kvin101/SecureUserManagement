package com.security.secureusermanagement.dao.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "address")
public class Address {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "street")
  private String street;

  @Column(name = "suite")
  private String suite;

  @Column(name = "city")
  private String city;

  @Column(name = "zipcode")
  private String zipcode;

  @OneToOne(mappedBy = "address")
  private User user;

  public Address(String street, String suite, String city, String zipcode) {
    this.street = street;
    this.suite = suite;
    this.city = city;
    this.zipcode = zipcode;
  }

  @JsonIgnore
  public Long getId() {
    return id;
  }
}
