def main():	
	server = ReportServer(".")
	server.render_html()
	server.set_free_port()
	server.serve_thread(uptime=int(uptime))

if __name__ == "__main__":
	main()


