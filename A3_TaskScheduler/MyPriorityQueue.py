from queue import PriorityQueue

class MyPriorityQueue(PriorityQueue):
	def __init__(self):
		super().__init__()
		self.counter = 0

	def insert(self, priority, item):
		super().put((priority, self.counter, item))
		self.counter += 1

	def remove(self, *args, **kwargs):
		_, _, item = super().get(self, *args, **kwargs)
		return item

	def peek(self):
		if not super().empty():
			return self.queue[0][2]			# location of the cargo is at index no. 2
		else:
			print("The queue is empty")
			return




