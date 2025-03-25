#!/usr/bin/python
import requests 
import sys
import argparse
from rich import print, print_json
sess = requests.Session()

def main():
    parser = argparse.ArgumentParser(prog="test.py", description="test utility")
    parser.add_argument("--base", "-b", default="http://localhost:8080/")
    parser.add_argument("--api", "-a", action="append", nargs=3)
    parser.add_argument("--login", "-l", nargs=2, default=["system", "123456"])
    parser.add_argument("--para", "-p", nargs=2, action="append")
    parser.add_argument("--no-login", "-j", action="store_true")
    args = parser.parse_args(sys.argv[1:])
    base = args.base
    if not args.no_login:
        print("[green]>>> Login as {}... [/green]".format(args.login[0]))
        resp = sess.post(base + "/login/loginNoCode", data={
                "loginname": args.login[0],
                "pwd": args.login[1]
            }).json()
        if not resp["code"] == 200:
            print("[red]>>> Login failed, exiting...[/red]")
            print(resp)
            return
        print("[green]>>> Login success[/green]")
    para_index = 0
    for api in args.api:
        url = base + api[0]
        method = api[1]
        para_cnt = int(api[2])
        print("[green]>>> ----- TESTING API {} -----[/green]".format(url))
        print("[green]>>> Method: {}[/green]".format(method))
        print("[green]>>> Args: {}[/green]".format("" if not para_cnt == 0 else "No Args."))
        paras = {}
        for i in range(para_cnt):
            para = args.para[para_index]
            print("[green]>>> {}: {}[/green]".format(para[0], para[1]))
            paras[para[0]] = para[1]
            para_index += 1
        resp = None
        if method == "POST":
            resp = sess.post(url, data=paras)
        elif method == "get":
            resp = sess.get(url, params=paras)
        else:
            resp = sess.request(method, url, params=paras)
        print("[green]>>> Response: [/green]")
        try:
            print_json(data=resp.json())
        except:
            print(resp.text)


if __name__ == "__main__":
    main()
