import requests
from flask import Response, json

def forward_request(forward_base_url, request):
    """forward the request to determined url and return a Flask.Response"""
    # cleansing forward_base_url to work with this method
    while forward_base_url[-1] == '/':
        forward_base_url = forward_base_url[:-1]

    # get the Uri to from forward uri
    if 'Uri' in request.headers:
        store_uri = '/' + request.headers['Uri']
    else:
        store_uri = ""
    url = forward_base_url + store_uri + request.path

    # get the method
    method = request.method

    # cleansing headers to prevent 400 bad request issue
    accepted_keys = ["Accept", "Accept-Encoding", "Accept-Language",
                     "Connection", "Host", "Referer", "X-Requested-With"]
    headers = {header[0]:header[1]
               for header in request.headers
               if header[0] in accepted_keys}

    # cleansing data
    data = request.data
    if len(data) == 0:
        data = None

    #forward request
    print("forwarding request to " + url)
    resp = requests.request(method=method,
                            url=url,
                            headers=headers,
                            data=data)

    # cleansing resp headers to prevent issue
    if 'Transfer-Encoding' in resp.headers:
        del resp.headers['Transfer-Encoding']

    return Response(headers=dict(resp.headers),
                    response=resp.text,
                    status=resp.status_code)
