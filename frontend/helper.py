import requests
from flask import Response, json

def forward_request(forward_base_url, request):
    """forward the request to determined url and return a Flask.Response"""
    while forward_base_url[-1] == '/':
        forward_base_url = forward_base_url[:-1]

    if 'Uri' in request.headers:
        store_uri = '/' + request.headers['Uri']

    else:
        store_uri = ""

    headers = request.headers
    method = request.method
    if method == 'DELETE':
        headers = None

    url = forward_base_url + store_uri + request.path
    data = request.data
    if len(data) == 0:
        data = None

    # print(request.headers)
    # print("Forwarding request to " + url)
    resp = requests.request(method=method,
                            url=url,
                            headers=headers,
                            data=data)

    # print resp.text
    if 'Transfer-Encoding' in resp.headers:
        del resp.headers['Transfer-Encoding']
    return Response(headers=dict(resp.headers),
                    response=resp.text,
                    status=resp.status_code)
