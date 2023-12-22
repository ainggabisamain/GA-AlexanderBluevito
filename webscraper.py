#Alexander Bleuvito Fevrier - 6182001033

from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from webdriver_manager.chrome import ChromeDriverManager
from selenium.webdriver.common.by import By
from selenium.webdriver.common.keys import Keys

import time

driver = webdriver.Chrome()

url = "https://www.puzzle-minesweeper.com/mosaic-5x5-easy/"

x = 40 #berapa kali mau scraping

for counter in range (x):
    driver.get(url) #buka halaman puzzle mosaic
    
    number_div = driver.find_elements(By.CLASS_NAME, "number") #ambil semua angka pada tabel minesweeper
    
    text = "" #hasil akhir
    counter = 0 #counter untuk menghindari menangkap div pertama yang tidak valid
    for item in number_div:
        number = item.text #ambil text dari div
        if number == '': #bila kosong tulis menjadi -1
            number = -1
        
        if counter != 0: #bila bukan elemen div pertama maka div adalah valid
            text = text+str(number)+" " #concat ke text
            if counter%5 == 0: #print line bila merupakan number ke 5, sehingga hasil text menyerupai Mosaic pada halaman web
                text = text+'\n'
        
        counter += 1

    print(text) #print ke terminal

    time.sleep(5) #untuk membandingkan hasil di terminal dengan puzzle di web. comment bila dirasa tidak perlu

driver.close() 
driver.quit() #tutup webdriver