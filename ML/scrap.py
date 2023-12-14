import time
import os
import json
from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.common.by import By
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.common.action_chains import ActionChains

from pyvirtualdisplay import Display
from fake_useragent import UserAgent

from dotenv import load_dotenv
load_dotenv()

CHROMEDRIVER_PATH = os.getenv("CHROMEDRIVER_PATH")


def opener(name, path):
    # chromePath = os.getenv("CHROME_PATH")
    service = Service(CHROMEDRIVER_PATH)

    # options = webdriver.ChromeOptions()
    # profile = path + "\PROFILE" + "\\" + "Profile " + name
    # argument = '--user-data-dir=' + profile
    # options.add_argument(argument)
    # options.add_argument('--ignore-certificate-errors')
    # options.add_argument('--ignore-ssl-errors')
    # # maximize window
    # options.add_argument("--start-maximized")
    # # headless
    # # options.add_argument("--headless")
    # options.add_experimental_option("excludeSwitches", ["enable-logging"])
    # # driver = webdriver.Chrome(chromePath)
    # driver = webdriver.Chrome(service=service, options=options)

    
    ua = UserAgent()
    userAgent = ua.chrome
    options = webdriver.ChromeOptions()
    profile = path + "\PROFILE" + "\\" + "Profile " + name
    argument = '--user-data-dir=' + profile
    options.add_argument(argument)
    options.add_argument(f'user-agent={userAgent}')
    options.add_argument('--no-sandbox')
    options.add_argument('--window-size=1920,1080')
    options.add_argument('--headless')
    options.add_argument('--disable-gpu')
    options.add_argument('--allow-running-insecure-content')
    options.add_argument("--headless")
    options.add_argument('--ignore-certificate-errors')
    options.add_argument('--ignore-ssl-errors')
    driver = webdriver.Chrome(service=service, options=options)


    return driver

def scrap(keyword):
    driver = opener("1", os.path.dirname(os.path.realpath(__file__)))
    # driver.get("https://iprice.co.id/")

    domain = "https://iprice.co.id"
    # replace space with +
    keySearch = keyword.replace(" ", "+")
    # search keyword
    driver.get(domain + "/search/?term=" + keySearch)

    # driver.get("https://iprice.co.id/search/?term=minyak+kayu+putih+caplang+120ml")
    time.sleep(5)


    # get body html
    # body = driver.find_element(By.TAG_NAME, 'body').get_attribute("innerHTML")
    # write to file
    # with open('iprice2.html', 'w', encoding='utf-8') as f:
    #     f.write(body)

    dataCollected = []

    # get all card 
    cards = driver.find_elements(By.CLASS_NAME, 'zA')
    # print(len(cards))
    for card in cards:
        name = card.find_element(By.CLASS_NAME, 'zE').text
        price = card.find_element(By.CLASS_NAME, 'z4').text
        dataCollected.append({
            "name": name,
            "price": price
        })

    dataToWrite = {
        "keyword": keyword,
        "data": dataCollected
    }
    
    with open('dataRawScrap.json', 'w', encoding='utf-8') as f:
        json.dump(dataToWrite, f, ensure_ascii=False, indent=4)

    # range the of price
    rangePrice = []
    for data in dataCollected:
        price = data["price"]
        price = price.replace("Rp", "")
        price = price.replace(".", "")
        price = int(price)
        rangePrice.append(price)

    # sort the price
    rangePrice.sort()

    # get q1 and q2 of price
    q1 = int(len(rangePrice) * 0.25)
    q2 = int(len(rangePrice) * 0.50)

    # anti dus or cluster
    if rangePrice[q1]*2 > rangePrice[q2]:
        print("anti dus or cluster")
        pass
    else:
        print("anti dus or cluster2")
        # remove items that have price more than 2x of the q1
        for i in range(len(rangePrice)):
            if rangePrice[i] > rangePrice[q1]*2:
                rangePrice[i] = 0
        
        # remove 0 from list
        rangePrice = list(filter((0).__ne__, rangePrice))
        
        # get q1 and q2 of price
        q1 = int(len(rangePrice) * 0.25)
        q2 = int(len(rangePrice) * 0.50)
    
    

    print(rangePrice[q1])
    print(rangePrice[q2])

    # check if q1 and q2 is same
    if q1 == q2:
        q2 = q1 + 500

    # check if there is dataFinal.json
    if os.path.isfile('dataFinal.json'):
        with open('dataFinal.json') as f:
            dataFinal = json.load(f)
    else:
        dataFinal = []

    dataWrite = {
        "keyword": keyword,
        "Harga Beli": rangePrice[q1],
        "Harga Jual": rangePrice[q2]
    }

    dataFinal.append(dataWrite)

    with open('dataFinal.json', 'w', encoding='utf-8') as f:
        json.dump(dataFinal, f, ensure_ascii=False, indent=4)

    with open('rangePrice.json', 'w', encoding='utf-8') as f:   
        json.dump(rangePrice, f, ensure_ascii=False, indent=4)

scrap("La Fonte Spaghetti Pasta 450 G")