package tamagotchi.domain;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import tamagotchi.dao.FakePetDao;

/**
 *
 * @author Heli
 */
public class PetCareTest {
    PetCare petCare;
    
    public PetCareTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        this.petCare = new PetCare(new FakePetDao());
    }
    
    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
    public void hello() {}
     
    @Test
    public void feedPetIncreasesEnergyRight() {
        int originalValue = this.petCare.getPet().getEnergy().getValue();
        this.petCare.feedPet();
        int newValue = this.petCare.getPet().getEnergy().getValue();
        assertTrue(newValue - originalValue == 10);
    }
    
    @Test
    public void healPetIncreasesHealthRight() {
        int originalValue = this.petCare.getPet().getHealth().getValue();
        this.petCare.healPet();
        int newValue = this.petCare.getPet().getHealth().getValue();
        assertTrue(newValue - originalValue == 10);
    }
    
    @Test
    public void getPetReturnsTheCorrectPet() {
        Pet pet = new Pet();
        assertTrue(pet != this.petCare.getPet());
    }
    
    @Test
    public void getPetDoesNotReturnNull() {
        assertTrue(this.petCare.getPet() != null);
    }
    
    @Test
    public void cleanPetSetsPetHygieneCorrectly() {
        this.petCare.cleanPet();
        assertTrue(this.petCare.getPet().getHygiene().getValue() == 100);
    }
    
    @Test
    public void petDoesNotGetSickIfHealthIsMaxed() {
        this.petCare.getPet().getHealth().setValue(100);
        this.petCare.checkIfPetGetsSick();
        assertTrue(!this.petCare.getPet().getIsSick());
    }
    
    @Test
    public void petGetsSickIfHealthIsZero() {
        this.petCare.getPet().getHealth().setValue(0);
        this.petCare.checkIfPetGetsSick();
        assertTrue(this.petCare.getPet().getIsSick());
    }
}