import msoffcrypto
from string import ascii_lowercase, ascii_uppercase
from itertools import product
import time

charset = ascii_uppercase + ascii_lowercase # ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz
maxrange = 20
encrypted = open("hola.xls", "rb")
file = msoffcrypto.OfficeFile(encrypted)

def solve_password(maxrange):
    isCracked = False
    for i in range(0, maxrange + 1):
        print("Loop #: ", i)
        if (isCracked):
            break
        for attempt in product(charset, repeat=i):
            tmpPass = ''.join(attempt)
            try:
                file.load_key(tmpPass)  # Open the file using temporal password
                print("Password correct: ", attempt) #Array
                print("password: ", tmpPass) #String
                isCracked = True
                break
            except:
                print("Exception opening the file, password incorrect: ", attempt)

start_time = time.time()
print("charset: ", charset)
solve_password(maxrange)

execution_time = time.time() - start_time
print("--- %s seconds ---" % execution_time)
print("--- %s min ---" % (execution_time / 60))