# Author: Jerry Shafer
# reference: https://www.geeksforgeeks.org/performing-google-search-using-python-code/

import sys

try: 
    from googlesearch import search 
except ImportError:  
    print("No module named 'google' found") 
  
query = sys.argv[1]
results = []
  
for result in search(query, tld="com", num=10, start=0, stop=1, pause=2.0): 
    # print(result)
	results.append(result)

ctr = 0
while ctr < len(results):
	if "stackoverflow.com" in results[ctr]: 
		result = results[ctr]
		break
	ctr += 1
if ctr == len(results):
	result = "No search results found."

print (result)	
#return result