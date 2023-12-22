/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */
package com.example.geneticalgorithm;

/**
 *
 * Sumber Referensi : 
 * - https://www.selenium.dev/documentation/webdriver/getting_started/first_script/
 */
import io.github.bonigarcia.wdm.WebDriverManager;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class Scraper {

    private String BASE_URL;
    private final WebDriver driver;
    private final File file;
    private FileWriter fileWriter;

    public Scraper(File file, int pilihan) {
        driver = WebDriverManager.chromedriver().create();

        this.file = file;
        
        if(pilihan == 1){ //sesuaikan url untuk membuka website mosaic puzzle yang diinginkam pengguna
            this.BASE_URL = "https://www.puzzle-minesweeper.com/mosaic-5x5-easy/";
        }
        else if(pilihan == 2){
            this.BASE_URL = "https://www.puzzle-minesweeper.com/mosaic-5x5-hard/";
        }
        else if(pilihan == 3){
            this.BASE_URL = "https://www.puzzle-minesweeper.com/mosaic-7x7-easy/";
        }
        else if(pilihan == 4){
            this.BASE_URL = "https://www.puzzle-minesweeper.com/mosaic-7x7-hard/";
        }
        else if(pilihan == 5){
            this.BASE_URL = "https://www.puzzle-minesweeper.com/mosaic-10x10-easy/";
        }
        else if(pilihan == 6){
            this.BASE_URL = "https://www.puzzle-minesweeper.com/mosaic-10x10-hard/";
        }
        else if(pilihan == 7){
            this.BASE_URL = "https://www.puzzle-minesweeper.com/mosaic-15x15-easy/";
        }
        else if(pilihan == 8){
            this.BASE_URL = "https://www.puzzle-minesweeper.com/mosaic-15x15-hard/";
        }
        else if(pilihan == 9){
            this.BASE_URL = "https://www.puzzle-minesweeper.com/mosaic-20x20-easy/";
        }
        else{//pilihan nomor 10
            this.BASE_URL = "https://www.puzzle-minesweeper.com/mosaic-20x20-hard/";
        }
    }

    public void tearDown() {
        driver.close();
        driver.quit();
    }

    public void execute(int times) {
        try {
            String testCases = ""; //menampung semua test case
            String line;
            while (times > 0) { //melakukan scraping sebanyak input
                driver.get(BASE_URL); //buka website
                driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500)); //tunggu agar semua elemen web sudah tersedia

                List<WebElement> number_div = driver.findElements(By.className("number")); //ambil semua elemen div dengan kelas "number"

                line = getLine(number_div); //satu baris test case
                
                testCases += line;

                int timeToSleepMillis = 0 * 1000; //berapa lama waktu pause
                Thread.sleep(timeToSleepMillis); //pause webdriver sementara untuk membandingkan hasil scrape dengan mosaic puzzle di web

                times--;
            }
            //System.out.print(testCases);
            writeToFile(testCases); //tulis semua test case ke sebuah file
            tearDown();
        } catch (InterruptedException ex) { //menangkap exception yang dilempar oleh Thread.sleep atau filewriter
            Logger.getLogger(Scraper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String getLine(List<WebElement> number_div) {
        boolean invalidCell = true;
        String line = ""; //string untuk menampung sebaris test case
        String number;
        for (WebElement element : number_div) { //ambil text dari semua elemen div text dari div ini merupakan nilai puzzle minesweeper
            number = element.getText();
            if (!invalidCell) { //jumlah div yang terdeteksi adalah 26. sementara tabel hanya berisi 25 sel. div pertama adalah div yang invalid
                if (number.equals("")) { //sel kosong
                    number = "-1";
                }
                line += number+" ";
            } else {
                invalidCell = false;
            }

        }
        //line = line.substring(0, line.length()-1); //menghilangkan di ujung test case
        line += '\n'; //menambah karakter new line di ujung test

        return line;
    }

    private void writeToFile(String testCases) {
        try {
            fileWriter = new FileWriter(file, true); //instansiasi objek FileWriter, dengan mode append (menambah line ke file)
            fileWriter.write(testCases); //menulis tes kasus-tes kasus ke file 

            fileWriter.flush();
            fileWriter.close(); //tutup filewriter
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
